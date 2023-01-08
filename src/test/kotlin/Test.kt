import java.lang.StringBuilder
import kotlin.test.Test

class Test {

    @Test
    fun test(){
//        val str1 = "homes.commands.*"
//        val str2 = "homes.commands.homes.create"

//        if(str2.contains(str1.substringBeforeLast('*').substringBeforeLast('.'))) println(true)

        var sb = StringBuilder("commands.hadda")
        sb = StringBuilder("luckperms.").append(sb)

        println(sb.toString())
    }

}