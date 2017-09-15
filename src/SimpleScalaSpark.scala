import org.apache.spark.SparkContext
import org.apache.spark.SparkConf

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


    var testClass3 = new TestSequences()
    var seqeunces = testClass3.setTestSequences()
    val rdd = sc.parallelize(seqeunces)
    println(rdd.filter(_.idSequence ==1).count)
    var aa = rdd.filter(_.idSequence ==1).collect().toList
    var testClass4 = new AggregateSequences()
    var bb = testClass4.aggragate(aa, 500,listSel,listSel2,listWhe,listWhe2)
    bb.printSequence()
  }

}
