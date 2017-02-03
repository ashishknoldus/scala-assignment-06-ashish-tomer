package edu.knoldus.filer

import java.io.{File, FileNotFoundException}

import org.scalatest.{BeforeAndAfterAll, FunSuite}

import scala.concurrent.{Await, Future}
import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.duration._

/**
  * Created by ashish on 2/3/17.
  */
class FileListerTest extends FunSuite with BeforeAndAfterAll{


  test("Testing correct output") {

    val fileLister = new FileLister
    val actualList = Await.result(fileLister.listFiles("Folder2"), 2 seconds)

    val file6 = "Folder4/file6/"
    val file7 = "Folder4/Folder5/file7/"
    val file8 = "Folder4/Folder5/file8/"
    val file4 = "Folder3/file4/"
    val file5 = "Folder3/file5/"
    val file2 = "file2/"
    val file3 = "file3/"

    val expectedList = List(file6, file7, file8, file4, file5, file2, file3)

    assert(actualList == expectedList)

  }

  test("Test NullPointerException while accessing file") {
    val fileLister = new FileLister
    //Wrong directory name
    intercept[NullPointerException] {
      Await.result(fileLister.listFiles("Folder0"), 2 seconds)
    }
  }

  test("Test Count of Files") {
    val numberOfFiles = 7
    val fileLister = new FileLister
    val actualList = Await.result(fileLister.listFiles("Folder2"), 2 seconds)

    assert(actualList.length == numberOfFiles)
  }


}
