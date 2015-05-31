import java.text.SimpleDateFormat

import org.joda.time.DateTime
val pattern = "dd/MM/yyyy HH:mm:ss'";
val d1 = new DateTime()
d1.toString(pattern)


//val f = for {
//  i <- Seq[Int]
//  v <- (0 to i)
//} yield println(v)

val l = List(1, 2 ,3)

l.find( (i: Int) => i == 3)

val opt: Option[Int] = Some(1)
//val opt: Option[Int] = None
opt.map {
  i: Int => i + 1
}.getOrElse {
  println("Not Found")
}

opt.map(i => i + 1).getOrElse(println("Not Found"))

"1, 2, 3".split(",").toList.sorted

" all this chars: (1,2,4)".r match {
  case : Int => println(n)
}



