import scala.collection.mutable.ArrayBuffer
import java.util.Scanner

object MergeTransactions {
	
	def mergeUnsigned(input : String) : Transaction = {
	  var transactions = stringToTransactions(input)
	  var unsigned = mergeUnsignedTransactions(transactions)
	  if(unsigned == null){
	    println("Merging failed") //reasons why it failed are printed out by mergeUnsignedTransactions
	    unsigned
	  }
	  else{
	    unsigned
	  }
	}
	
	def mergeSigned(input : String) : Transaction= {
	  var transactions = stringToTransactions(input)
	  var signed = mergeSignedTransactions(transactions)
	  if(signed == null){
	    println("Merging failed") //reasons why it failed are printed out by mergeUnsignedTransactions
	    signed
	  }
	  else{
	    signed
	  }
	}
	
	//take in as an input several transactions, one per line, inputted as hex strings
	//returns an array of the parsed transactions
	def stringToTransactions(input : String) : ArrayBuffer[Transaction] = {
		var read = new Scanner(input)
		var transactions =  new ArrayBuffer[Transaction](0)
		while (read.hasNextLine) {
			var line = read.nextLine();
			var parser = new TransactionParser(line)
			transactions += parser.parseTransaction
		}
		read.close
		transactions
	}
	
	//Check if two inputs are the same
	def areEqualInputs(i1 : TransactionInput, i2 : TransactionInput) = 
	  ((i1.getPrevHash.deep == i2.getPrevHash.deep) && (i1.getIndex == i2.getIndex))  && (i1.getSeqNum == i2.getSeqNum)
	
	//Check if two outputs are the same
	def areEqualOutputs(o1 : TransactionOutput, o2 : TransactionOutput) = 
	  ((o1.getValue == o2.getValue) && (o1.getScript.deep == o2.getScript.deep))
	  
	//merge raw transactions sent in from the users
	//send back for signing and approval
	def mergeUnsignedTransactions(transactions : ArrayBuffer[Transaction]) = {
	  //empty list return null
	  if(transactions.length==0){null}
	  //use these to check for correctness of other transactions in the array
	  var checkVersion = transactions(0).getTransactionVersion
	  var checkTime = transactions(0).getTransactionLockTime
	  //this transaction will have the collated inputs and outputs
	  var finalTx = new Transaction(checkVersion, 0, new ArrayBuffer[TransactionInput](),
	     0, new ArrayBuffer[TransactionOutput](), checkTime)
	  for(t <- transactions){
	    if(t.getTransactionLockTime != checkTime){
	      println("Incompatible lock times in inputted transactions")
	      null //can't merge transactions
	    }
	    if(t.getTransactionVersion!=checkVersion){
	      println("Incompatible transaction version in inputted transactions")
	      null //can't merge transactions
	    }
	    //Collate outputs
	    //Duplicate outputs are allowed
	    //Duplicate inputs are not allowed
	    for(out <- t.getOutputs){
	      var alreadyExists = false
	      for(o <- finalTx.getOutputs){
	    	//perform deep array comparison
	        if(out.getScript.deep ==  o.getScript.deep){alreadyExists = true}
	      }
	      if(!alreadyExists){
	        finalTx.addOutput(out)
	        finalTx.addOutputCount(1)
	      }
	    }
	    //Collate inputs
	    for(in <- t.getInputs){
	      var alreadyExists = false
	      for(i <- finalTx.getInputs){
	        if(areEqualInputs(in,i)){alreadyExists = true}
	      }
	      if(!alreadyExists){
	        finalTx.addInput(in)
	        finalTx.addInputCount(1)}
	      //duplicate input
	      else{
	        println("Duplicate inputs found, invalid transaction to merge")
	        null //invalid
	      }
	    }
	  }
	  //RANDOMIZE COLLATED INPUTS AND OUTPUTS
	  finalTx
	}
	
	def mergeSignedTransactions (transactions : ArrayBuffer[Transaction]) : Transaction = {
	  //empty list return null
	  if(transactions.length==0){null}
	  var checkTransaction = transactions(0) //first transaction serves as checker
	  var checkVersion = checkTransaction.getTransactionVersion
	  var checkTime = checkTransaction.getTransactionLockTime
	  //this transaction will have the collated inputs and outputs
	  var finalTx = new Transaction(checkVersion, 0, new ArrayBuffer[TransactionInput](),
	     0, new ArrayBuffer[TransactionOutput](), checkTime)
	  
	  for(t <- transactions){
	    if(t.getTransactionLockTime != checkTime){
	      println("Incompatible lock times in inputted transactions")
	      null //can't merge transactions
	    }
	    if(t.getTransactionVersion!=checkVersion){
	      println("Incompatible transaction version in inputted transactions")
	      null //can't merge transactions
	    }
	    
	    //matching outputs
	    for(i<- 0 to t.getOutputs.length - 2){
	      if(!areEqualOutputs(t.getOutputs(i), t.getOutputs(i+1))){
	         println("Outputs number: " + i + " and : " + (i+1) +"do not match")
	         null //can't merge transactions
	      }
	      else{
	        finalTx.addOutput(t.getOutputs(i))
	        finalTx.addOutputCount(1)
	        if(i == t.getOutputs.length -2){
	          finalTx.addOutput(t.getOutputs(i+1))
	          finalTx.addOutputCount(1)
	        }
	      }
	    }

	    //matching inputs
	    for(i<- 0 to t.getInputs.length - 2){
	      if(!areEqualInputs(t.getInputs(i), t.getInputs(i+1))){
	         println("Inputs number: " + i + " and : " + (i+1) +"do not match")
	         null //can't merge transactions
	      }
	      //inputs match -- take the signature
	      else{
	    	 //if there is an input script copy it over to the next input
	    	 if(t.getInputs(i).getScript.length>0){
	    	   t.getInputs(i+1).setScript(t.getInputs(i).getScript)
	    	 }
	    	 finalTx.addInput(t.getInputs(i))
	    	 finalTx.addInputCount(1)
		     if(i == t.getInputs.length -2){
		       finalTx.addInput(t.getInputs(i+1))
		       finalTx.addInputCount(1)
		     }
	      }
	    }
	  }
	  finalTx
	}
	
}