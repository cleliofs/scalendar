import java.text.SimpleDateFormat

import org.joda.time.DateTime
val pattern = "dd/MM/yyyy HH:mm:ss'";
val d1 = new DateTime()
d1.toString(pattern)


//val f = for {
//  i <- Seq[Int]
//  v <- (0 to i)
//} yield println(v)
