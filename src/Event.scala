class Event (idS: Integer, time: Integer,attribs: scala.collection.mutable.Map[String, String])  extends Serializable{
    var timestamp: Integer = time
    var idSequence: Integer = idS
    var attributes: scala.collection.mutable.Map[String, String] = attribs
}
