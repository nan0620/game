// @GENERATOR:play-routes-compiler
// @SOURCE:/Users/nan/Downloads/IT-Project/team-project/ITSD-DT2021-Template/conf/routes
// @DATE:Wed Jul 28 23:15:52 BST 2021


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
