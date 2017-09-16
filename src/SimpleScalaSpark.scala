import org.apache.spark.SparkContext
import org.apache.spark.SparkConf

import scala.collection.mutable.ListBuffer

object SimpleScalaSpark {

  def main(args: Array[String]) {
    val logFile = "E:/Apache_Spark/spark-2.1.1-bin-hadoop2.7/README.md" // Should be some file on your system
    val conf = new SparkConf().setAppName("Simple Application").setMaster("local[*]")
    val sc = new SparkContext(conf)
    val logData = sc.textFile(logFile, 2).cache()
    val numAs = logData.filter(line => line.contains("a")).count()
    val numBs = logData.filter(line => line.contains("b")).count()
    println("Lines with a: %s, Lines with b: %s".format(numAs, numBs))


    /* val test = "max( prad ) AS max_prad, concat( prad_text ) AS concat_prad_text"
    val test2 = "max( pradI ) AS max_pradI,max( pradI_div divide ) AS max_pradI_div, concat( pradI ) AS concat_prad_textI"*/
    val test = "avg( prad ) AS avg_prad, min( prad ) AS min_prad, max( prad ) AS max_prad, concat( prad_text ) AS concat_prad_text"
    val test2 = "avg( pradI ) AS avg_pradI, min( pradI ) AS min_pradI, max( pradI divide ) AS max_pradI, concat( pradI_text ) AS concat_pradI_text"
    //val test3 = "prad < 500,prad_text == 'Alaska'"
    val test3 = ""
    //val test4 = "pradI < 500"
    val test4 = ""
    var testClass = new SelectParser()
    val listSel = testClass.parseEvent(test)
    val listSel2 = testClass.parseInterval(test2)
    var testClass2 = new WhereParser()
    val listWhe = testClass2.parseWhere(test3)
    val listWhe2 = testClass2.parseWhere(test4)
/*

    var testClass3 = new TestSequences()
    var seqeunces = testClass3.setTestSequences()
    val rdd = sc.parallelize(seqeunces)
    println(rdd.filter(_.idSequence == 1).count)
    var aa = rdd.filter(_.idSequence == 1).collect().toList
    var testClass4 = new AggregateSequences()
    var bb = testClass4.aggregate(aa, 500,test,test2,test3,test4)
    bb.printSequence()*/


    val files: List[String] = List("power_11", "power_12","power_13","power_21","power_22","power_23","power_31","power_32","power_33","power_41","power_42","power_43","power_51","power_52","power_53")

    import scala.collection.mutable.ListBuffer

    var seqBufList = new ListBuffer[EventIntervalSequence]()

    val importSeq = new ImportSequence()

    for(f <- files){
      seqBufList += importSeq.importSequence("C:/Users/adamc/Desktop/" + f + ".txt")
    }

    for(se <- seqBufList) {
      println(se.idSequence)
    }


    val selEv = "concat( type_device ) AS concat_type_device"

    val selInt = "avg( power_consum ) AS avg_power_consum, min( power_consum ) AS min_power_consum, max( power_consum divide ) AS max_power_consum_div, max( power_consum ) AS max_power_consum, concat( status ) AS concat_status"

    val whEv = ""

    val whInt = "power_consum != 0"


    val rdd = sc.parallelize(seqBufList.toList)


    println(rdd.filter(_.idSequence < 41).count)

    var aggSeq = new AggregateSequences()



    var testSeq = rdd.filter(_.idSequence < 41).collect().toList

    var seqFirst = aggSeq.aggregate(testSeq, 80,selEv,selInt,whEv,whInt)

    seqFirst.exportSequenceToFile("power", "C:/Users/adamc/Desktop/")


    var testSeq2 = rdd.filter(_.idSequence >= 41).collect().toList

    var seqSecond = aggSeq.aggregate(testSeq2, 81,selEv,selInt,whEv,whInt)

    seqSecond.exportSequenceToFile("power", "C:/Users/adamc/Desktop/")



    val selInt2 = "avg( avg_power_consum ) AS avg_power_consum_full, concat( concat_status ) AS concat_status_full"

    var seqFull = aggSeq.aggregate(List(seqFirst,seqSecond), 82,"",selInt2,"","")

    seqFull.exportSequenceToFile("power", "C:/Users/adamc/Desktop/")

  }
}
