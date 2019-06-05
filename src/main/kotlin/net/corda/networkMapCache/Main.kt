package net.corda.networkMapCache

import com.typesafe.config.ConfigFactory
import java.io.File
import java.sql.DriverManager
import kotlin.system.exitProcess


fun main(args: Array<String>) {
    val conf = ConfigFactory.parseFile(File("./dbconfig.conf"))

    val className = conf.getString("dataSourceProperties.dataSourceClassName")
    try {
        Class.forName(className)
    } catch(ex: Exception) {
        println(ex.message)
        exitProcess(1)
    }

    val cnn = DriverManager.getConnection(conf.getString("dataSourceProperties.dataSource.url"), conf.getString("dataSourceProperties.dataSource.user"), conf.getString("dataSourceProperties.dataSource.password"))
    val cmd = cnn.prepareStatement("DELETE FROM NODE_INFO WHERE CERT_SIGNING_REQUEST NOT IN (SELECT REQUEST_ID FROM CERT_SIGNING_REQUEST WHERE LEGAL_NAME LIKE '%Notary%')")
    cmd.execute()
    cnn.close()
}