import java.lang.Long
import java.math.BigInteger
object Test {
	
  def main(args: Array[String]) {
      var tx1 = "01000000013c0c37049cefb7d0754c716c1227e221f1b5cc9fdf7fc8e6aadd5ce6465fad32000000004a493046022100b41330548f320fcc282d72462656f80c0da64beb352f7fbbdf55d651674b5846022100cbef624c80302900e6c0e9b4bbb024cd072e54d7535c8a79a3ce9b36c304d7cc01ffffffff0100f2052a0100000017a914379ad9b7ba73bdc1e29e286e014d4e2e1f6884e38700000000\n"
      var tx2 = "010000000119f486a20df2078f6992e6c09eac14b0d6b3ec6d2095a2056ff58a55eacda756000000006a473044022064e78d02b2a298679cbd10d346088f9c0b7179e93ad2e097f05f8134d006e9ae0220393b0b7d8813205cccf47a4ab27f3fe5002c4204e2d9ca6651adcec976236c610121026100cfae965e2902b0cc55e032896255b3fd86e15818c4435d3e33c76bf761a4ffffffff01a0175200000000001976a914b69a83e41dc486f52e9d4b4bc42e57dae2c7210288ac00000000\n"
      var tx3 = "010000000183b478327a9305dc47b54dcf5529e797a8aec92d393d89117ccea1e1653ae2b70000000000ffffffff01c0aff629010000001976a914101895444929ead6795e04c5f6604ae96a1b852688ac00000000"
//      var res = MergeTransactions.mergeUnsigned(RandomTransactions.generateNTransactions(2))
      var a = RandomTransactions.generateNTransactions(15)
      MergeTransactions.mergeUnsigned(a).printTransaction
      println(MergeTransactions.mergeUnsignedHex(a))
//      if(res != null){
//    	  res.printTransaction
//      }
    }
}