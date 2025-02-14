import com.example.generated.PackBindings

fun main() {
    val model = PackBindings.SPEED.model
    //Spawn a new speed field using the predicates or something (in our case we print them)
    println(model.key)
    println(model.predicates?.map { it.name() to it.value() })
}