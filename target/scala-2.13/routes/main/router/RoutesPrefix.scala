// @GENERATOR:play-routes-compiler
// @SOURCE:C:/Users/Nan Jiang/Documents/ITProject/ITSD-DT2021-Template/conf/routes
// @DATE:Wed Jul 07 02:35:57 BST 2021


package router {
  object RoutesPrefix {
    private var _prefix: String = "/"
    def setPrefix(p: String): Unit = {
      _prefix = p
    }
    def prefix: String = _prefix
    val byNamePrefix: Function0[String] = { () => prefix }
  }
}
