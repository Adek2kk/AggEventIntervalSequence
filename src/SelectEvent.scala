class SelectEvent(operationPar: String, attributeName: String, newAttributeName: String) {
  var operation: String = operationPar
  var attrName: String = attributeName
  var newName: String = newAttributeName

  var firstValueFlag: Boolean = true
  var maxValue: Double = 0
  var minValue: Double = 0
  var avgValue: Double = 0
  var countValue: Double = 0
  var sumValue: Double = 0
  var stringValue: String = null

  def clear = {
    firstValueFlag = true
    maxValue = 0
    minValue = 0
    avgValue = 0
    countValue = 0
    sumValue = 0
    stringValue = null
  }


}
