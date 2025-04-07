import simpy
import random
import numpy as np
import matplotlib.pyplot as plt

random.seed(10)

class Paciente:
    def __init__(self, env, nombre, severidad):
        self.env = env
        self.nombre = nombre
        self.severidad = severidad  
        self.tiempos_espera = {}    
        self.tiempos_proceso = {}   

def generador_pacientes(env, interval, enfermeras, doctores, rayosX, analisis_sangre):
    i = 0
    while True:
        yield env.timeout(random.expovariate(1.0 / interval))
        severidad = random.randint(1, 5)  
        paciente = Paciente(env, f"Paciente {i}", severidad)
        env.process(atencion_paciente(env, paciente, enfermeras, doctores, rayosX, analisis_sangre))
        i += 1

def atencion_paciente(env, paciente, enfermeras, doctores, rayosX, analisis_sangre):
    
    with enfermeras.request(priority=paciente.severidad) as req:
        paciente.tiempos_espera['triage'] = env.now
        yield req
        yield env.timeout(10)  
        paciente.tiempos_proceso['triage'] = env.now

    with doctores.request(priority=paciente.severidad) as req:
        paciente.tiempos_espera['doctor'] = env.now
        yield req
        yield env.timeout(15) 
        paciente.tiempos_proceso['doctor'] = env.now

    
    necesita_rayosX = random.random() < 0.5
    necesita_analisis = random.random() < 0.5

    if necesita_rayosX:
        with rayosX.request(priority=paciente.severidad) as req:
            paciente.tiempos_espera['rayosX'] = env.now
            yield req
            yield env.timeout(20)  
            paciente.tiempos_proceso['rayosX'] = env.now

    if necesita_analisis:
        with analisis_sangre.request(priority=paciente.severidad) as req:
            paciente.tiempos_espera['analisis'] = env.now
            yield req
            yield env.timeout(25)  
            paciente.tiempos_proceso['analisis'] = env.now

    with doctores.request(priority=paciente.severidad) as req:
        paciente.tiempos_espera['evaluacion'] = env.now
        yield req
        yield env.timeout(10)  
        paciente.tiempos_proceso['evaluacion'] = env.now

    tiempo_total = env.now - paciente.tiempos_espera['triage']
    print(f"{paciente.nombre} (severidad {paciente.severidad}) salió a las {env.now}, tiempo total: {tiempo_total:.2f}")
    return tiempo_total

def simulacion(num_enfermeras, num_doctores, num_rayosX, num_analisis, interval, tiempo_simulacion):
    env = simpy.Environment()
    enfermeras = simpy.PriorityResource(env, capacity=num_enfermeras)
    doctores = simpy.PriorityResource(env, capacity=num_doctores)
    rayosX = simpy.PriorityResource(env, capacity=num_rayosX)
    analisis_sangre = simpy.PriorityResource(env, capacity=num_analisis)
    
    pacientes = []
    def capturar_tiempos(env, interval, enfermeras, doctores, rayosX, analisis_sangre):
        i = 0
        while True:
            yield env.timeout(random.expovariate(1.0 / interval))
            severidad = random.randint(1, 5)
            paciente = Paciente(env, f"Paciente {i}", severidad)
            tiempo = yield env.process(atencion_paciente(env, paciente, enfermeras, doctores, rayosX, analisis_sangre))
            pacientes.append((paciente, tiempo))
            i += 1
    
    env.process(capturar_tiempos(env, interval, enfermeras, doctores, rayosX, analisis_sangre))
    env.run(until=tiempo_simulacion)
    
    tiempos_totales = [t for _, t in pacientes]
    return np.mean(tiempos_totales) if tiempos_totales else float('inf')

configuraciones = [
    (2, 3, 1, 1),  
    (3, 4, 2, 1),  
    (4, 5, 2, 2)   
]

resultados = {'regular': [], 'fin_semana': [], 'festivo': []}
for config in configuraciones:
    num_enfermeras, num_doctores, num_rayosX, num_analisis = config
    resultados['regular'].append(simulacion(num_enfermeras, num_doctores, num_rayosX, num_analisis, 5, 1440))
    resultados['fin_semana'].append(simulacion(num_enfermeras, num_doctores, num_rayosX, num_analisis, 3, 1440))
    resultados['festivo'].append(simulacion(num_enfermeras, num_doctores, num_rayosX, num_analisis, 2, 1440))

plt.figure(figsize=(10, 6))
for tipo, tiempos in resultados.items():
    plt.plot([str(c) for c in configuraciones], tiempos, marker='o', label=tipo)
plt.xlabel('Configuración (enfermeras, doctores, rayosX, análisis)')
plt.ylabel('Tiempo promedio en el sistema (minutos)')
plt.title('Tiempos Promedio por Configuración y Tipo de Día')
plt.legend()
plt.grid(True)
plt.show()