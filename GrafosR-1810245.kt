import java.util.Stack
import java.io.File
import libGrafoR.*


typealias Graph<T> = Map<T, List<T>>

fun <T> Graph<T>.topologicalSort(): List<T> {
    val graph = this
    val explored = mutableSetOf<T>()
    val result = mutableListOf<T>() // cambio para que no dé error
    val stack = Stack<T>() 

    
    fun explore(startVertex: T) {
        stack.push(startVertex) 

        while (stack.isNotEmpty()) { // ahora se considera es una pila vacía
            val vertex = stack.peek()  // se obtiene el top de la pila

            if (vertex !in explored) {
                explored += vertex 

                
                for (successor in graph[vertex].orEmpty()) {
                    if (successor !in explored) {
                        stack.push(successor) 
                    } else if (successor !in result) {
                        error("Graph contains a cycle, topological sort not possible!")
                    }
                }
            } else {
                
                if (vertex !in result) {
                    result += vertex
                }
                stack.pop() // Eliminar el vértice de la pila
            }
        }
    }

    
    for ((vertex, _) in graph) {
        if (vertex !in explored) {
            explore(vertex) 
        }
    }

    return result.reversed() 
}

fun main() {

    val archivo = File("gantt.txt")
    val lineas = archivo.readLines()
    // La primera línea es el número de vértices
    val vertices = lineas[0].toInt()


    var grafodi = GrafoDirigidoCosto(vertices)
    // la segunda línea es el número de arcos
    val arcos = lineas[1].toInt()
    // Las siguientes líneas son las arcos


    // map con los valores
    var tareas = mutableMapOf< String, Int>(
    )
    var count = 0
    var index = vertices
    for ( i in 2 until 2 + vertices){
        var (tarea) = lineas[i].split(" ")
        tareas[tarea] = count
        count = count + 1
    }



    for (i in 2 + vertices until lineas.size) {

        var (inicio, fin, costoStr) = lineas[i].split(" ")

        var u = tareas[inicio] ?: throw IllegalArgumentException("Parada no encontrada: $inicio")

        // excepcón que indica que el punto de partida debe ser ser la usb
        //if ( i == 2 && u != 0){ throw Exception("El punto de partida debe ser la USB") }


        var v = tareas[fin] ?: throw IllegalArgumentException("Parada no encontrada: $fin")

        try{
            var costo = costoStr.toDouble()
            grafodi.agregarArcoCosto( ArcoCosto(u, v, costo))
        } catch ( e: Exception ){
            throw Exception("El costo no tiene el formato adecuado")
        }
    }


    val adjacencyMap = mutableMapOf<Int, List<Int>>()

    for (v in 0 until grafodi.obtenerNumeroDeVertices()) {
        val adjacentVertices = grafodi.adyacentes(v).map { it.sumidero() }.toList()
        adjacencyMap[v] = adjacentVertices
    }

    val result = adjacencyMap.topologicalSort()


    for (valor in result) {
        var tarea = String()

        for ( (clave,indice) in tareas){
            if ( valor == indice ){
                tarea = clave
            }
        
        }
    println(tarea)

    //println(result)
    }
}

