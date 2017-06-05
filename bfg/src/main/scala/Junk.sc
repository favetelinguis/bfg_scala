val cache = List(1,2,3,4)
val delta = List(2,4,5)

val cacheMap = cache.map(c => c->c).toMap
val deltaSync = delta.map(d => cacheMap.getOrElse(d, 0))
val deltaMap = delta.map(c => c->c).toMap
val cacheSync = cache.map(c => deltaMap.getOrElse(c, 0))

val aa = cache.sortWith((a, b) => a < b)
val bb = delta.sortWith((a, b) => a < b)
aa.zipAll(bb,0,0)

val cc = cache.toSet
val dd = delta.toSet
val ee = cc.union(dd).toList
val deltaSync2 = ee.map(d => cacheMap.getOrElse(d, 0))
val cacheSync2 = ee.map(c => deltaMap.getOrElse(c, 0))

case class Ff(a:Int,b:Int)
val seta = Set(Ff(1,2), Ff(2,2))
val setb = Set(Ff(2,2))
seta.diff(setb)
