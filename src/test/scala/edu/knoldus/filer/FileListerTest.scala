package edu.knoldus.filer

import org.scalatest.FunSuite

/**
  * Created by ashish on 2/3/17.
  */
class FileListerTest extends FunSuite{

  test("Testing directory access") {
    val fileLister = new FileLister
    fileLister.listFiles("Folder2")
  }


}
