import java.io.File
import kotlin.Comparator
import kotlin.reflect.KClass
import kotlin.reflect.KProperty1
import kotlin.reflect.full.memberProperties
import kotlin.reflect.full.primaryConstructor
import kotlin.system.measureNanoTime
import com.example.laundrysortercompatiblewithlinux.R
import com.example.laundrysortercompatiblewithlinux.KotlinFunctionLibrary.sort

fun main() {
    val content = loadData(R.raw.laundry_instructions_simple).split("\n")
    val list = mutableListOf<LaundryItem>()
    content.forEach {
        val list1 = it.split(",")
        list.add(
            LaundryItem(
                list1[0] ,
                list1[1],
                list1[2],
                list1[3],
                list1[4],
                list1[5],
                list1[6],
                list1[7],
                list1[8],
                list1[9],
                list1[10],
                list1[11]
            )
        )
    }
    list.sort(
        mapOf(
            LaundryItem::ArticleType as KProperty1<LaundryItem, Comparable<Any>?> to true,
            LaundryItem::Wash as KProperty1<LaundryItem, Comparable<Any>?> to true
        )
    )
    val outFile = File("sorted_wash.csv")
    val bufferedWriter = outFile.bufferedWriter()
    list.forEach {
        val value = "${it.ArticleName}," +
                "${it.CompanyName}," +
                "${it.ArticleType}," +
                "${it.Wash}," +
                "${it.Dry}," +
                "${it.Color}," +
                "${it.OnlyWithLikeColors}," +
                "${it.DrySpinIntensity}," +
                "${it.WashSpinIntensity}," +
                "${it.RemovePromptly}," +
                "${it.Handwash}," +
                it.DryClean
        bufferedWriter.appendLine(
            value
        )
        println(value)
    }
    bufferedWriter.close()
    /*val generateList = {
        mutableListOf(
            Test("a", 1, true),
            Test("g", 3, false),
            Test("b", 1, true),
            Test("f", 2, false),
            Test("c", 1, true),
            Test("e", 2, false),
            Test("d", 2, false),
        )
    }
    val list1 = mutableListOf<Long>()
    val list2 = mutableListOf<Long>()
    for(i in 1..100){
        for (i in 1..600) {
            list2.add(measureNanoTime {
                sort(
                    generateList(),
                    listOf(
                        Test::number as KProperty1<Test, Comparable<Any>>,
                        Test::string as KProperty1<Test, Comparable<Any>>
                    ),
                    listOf(true, true)
                )
            })
            list1.add(measureNanoTime {
                sort(
                    generateList(),
                    listOf(Test::number.name, Test::string.name),
                    listOf(true, false)
                )
            })
        }
    }
    println("Average 1: ${list1.average()}")
    println("Average 2: ${list2.average()}")
    System.out.printf("hello",8,8)*/
}


