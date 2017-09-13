import scala.collection.mutable.ListBuffer

class TestSequences {
  def setTestSequences(): List[EventIntervalSequance] = {

    var seqInnerList1 = new ListBuffer[EventsInterval]()

    var event = new Event(1, 1, collection.mutable.Map[String, String]("prad" -> "124", "prad_text" -> "Alaska"))
    var eventsList = new ListBuffer[Event]()
    eventsList += event
    var evInt = new EventsInterval(eventsList.toList, new Interval(1, collection.mutable.Map[String, String]("pradI" -> "124", "pradI_div" -> "100", "pradI_text" -> "ON")))
    seqInnerList1 += evInt

    event = new Event(1, 4, collection.mutable.Map[String, String]("prad" -> "134", "prad_text" -> "Alaska2"))
    eventsList = new ListBuffer[Event]()
    eventsList += event
    evInt = new EventsInterval(eventsList.toList, new Interval(1, collection.mutable.Map[String, String]("pradI" -> "114", "pradI_div" -> "90", "pradI_text" -> "OFF")))
    seqInnerList1 += evInt

    event = new Event(1, 8, collection.mutable.Map[String, String]("prad" -> "144", "prad_text" -> "Alaska3"))
    eventsList = new ListBuffer[Event]()
    eventsList += event
    evInt = new EventsInterval(eventsList.toList, new Interval(1, collection.mutable.Map[String, String]("pradI" -> "104", "pradI_div" -> "80", "pradI_text" -> "ON")))
    seqInnerList1 += evInt

    event = new Event(1, 10, collection.mutable.Map[String, String]("prad" -> "154", "prad_text" -> "Alaska4"))
    eventsList = new ListBuffer[Event]()
    eventsList += event
    //evInt = new EventsInterval(eventsList.toList, new Interval(1,collection.mutable.Map[String,String]("pradI" -> "94", "pradI_div" -> "70", "pradI_text" -> "OFF")))
    evInt = new EventsInterval(eventsList.toList, null)
    seqInnerList1 += evInt

    var sequence1 = new EventIntervalSequance(1, seqInnerList1.toList)
    //////////////////////////////////////////////////////////////////////
    var seqInnerList2 = new ListBuffer[EventsInterval]()

    var event2 = new Event(2, 2, collection.mutable.Map[String, String]("prad" -> "74", "prad_text" -> "Malibu"))
    var eventsList2 = new ListBuffer[Event]()
    eventsList2 += event2
    var evInt2 = new EventsInterval(eventsList2.toList, new Interval(1, collection.mutable.Map[String, String]("pradI" -> "22", "pradI_div" -> "500", "pradI_text" -> "ON")))
    seqInnerList2 += evInt2

    event2 = new Event(2, 6, collection.mutable.Map[String, String]("prad" -> "94", "prad_text" -> "Malibu2"))
    eventsList2 = new ListBuffer[Event]()
    eventsList2 += event2
    evInt2 = new EventsInterval(eventsList2.toList, new Interval(1, collection.mutable.Map[String, String]("pradI" -> "52", "pradI_div" -> "400", "pradI_text" -> "OFF")))
    seqInnerList2 += evInt2

    event2 = new Event(2, 9, collection.mutable.Map[String, String]("prad" -> "114", "prad_text" -> "Malibu3"))
    eventsList2 = new ListBuffer[Event]()
    eventsList2 += event2
    evInt2 = new EventsInterval(eventsList2.toList, new Interval(1, collection.mutable.Map[String, String]("pradI" -> "720", "pradI_div" -> "300", "pradI_text" -> "ON")))
    seqInnerList2 += evInt2

    event2 = new Event(2, 11, collection.mutable.Map[String, String]("prad" -> "134", "prad_text" -> "Malibu4"))
    eventsList2 = new ListBuffer[Event]()
    eventsList2 += event2
    //evInt2 = new EventsInterval(eventsList2.toList, new Interval(1,collection.mutable.Map[String,String]("pradI" -> "92", "pradI_div" -> "200", "pradI_text" -> "OFF")))
    evInt2 = new EventsInterval(eventsList2.toList, null)
    seqInnerList2 += evInt2

    var sequence2 = new EventIntervalSequance(2, seqInnerList2.toList)

    //////////////////////////////////////////////////////////////////////

    var seqInnerList3 = new ListBuffer[EventsInterval]()

    var event3 = new Event(3, 1, collection.mutable.Map[String, String]("prad" -> "10", "prad_text" -> "Oahu"))
    var eventsList3 = new ListBuffer[Event]()
    eventsList3 += event3
    var evInt3 = new EventsInterval(eventsList3.toList, new Interval(1, collection.mutable.Map[String, String]("pradI" -> "41", "pradI_div" -> "97", "pradI_text" -> "ON")))
    seqInnerList3 += evInt3

    event3 = new Event(3, 3, collection.mutable.Map[String, String]("prad" -> "20", "prad_text" -> "Oahu2"))
    eventsList3 = new ListBuffer[Event]()
    eventsList3 += event3
    evInt3 = new EventsInterval(eventsList3.toList, new Interval(1, collection.mutable.Map[String, String]("pradI" -> "42", "pradI_div" -> "87", "pradI_text" -> "OFF")))
    seqInnerList3 += evInt3

    event3 = new Event(3, 5, collection.mutable.Map[String, String]("prad" -> "30", "prad_text" -> "Oahu3"))
    eventsList3 = new ListBuffer[Event]()
    eventsList3 += event3
    evInt3 = new EventsInterval(eventsList3.toList, new Interval(1, collection.mutable.Map[String, String]("pradI" -> "43", "pradI_div" -> "77", "pradI_text" -> "ON")))
    seqInnerList3 += evInt3

    event3 = new Event(3, 7, collection.mutable.Map[String, String]("prad" -> "40", "prad_text" -> "Oahu4"))
    eventsList3 = new ListBuffer[Event]()
    eventsList3 += event3
    event3 = new Event(3, 7, collection.mutable.Map[String, String]("prad" -> "500", "prad_text" -> "Oahu4_2"))
    eventsList3 += event3
    //evInt3 = new EventsInterval(eventsList3.toList, new Interval(1,collection.mutable.Map[String,String]("pradI" -> "44", "pradI_div" -> "67", "pradI_text" -> "OFF")))
    evInt3 = new EventsInterval(eventsList3.toList, null)
    seqInnerList3 += evInt3

    var sequence3 = new EventIntervalSequance(3, seqInnerList3.toList)

    /////////////////////////////////////////////

    var sequencesEI = new ListBuffer[EventIntervalSequance]
    sequencesEI += sequence1
    sequencesEI += sequence2
    sequencesEI += sequence3
    var testSeq = sequencesEI.toList

    //sequence3.printSequence()

    return sequencesEI.toList
  }
}
