package edu.knoldus.filer

import java.io.File

import scala.concurrent.Future
import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Await
import scala.concurrent.duration._

/**
  * Created by ashish on 2/2/17.
  */

class FileLister {

  def listFiles(directory : String): Array[Future[List[File]]] = {

    val classLoader:ClassLoader = getClass.getClassLoader
    val rootDirectory:File = new File(classLoader.getResource(directory).getFile)

    val listOfFiles:Future[Array[File]] = Future{ filesInDirectory(rootDirectory) }

    //used .value.get.get to get Array out of Future
    val allFiles:Array[Future[List[File]]] = Await.result(Future{listOfFiles}, 2.seconds).value.get.get.map(file => {

      Future{ reachFromFileToRoot(List[File](file), rootDirectory) }

    })

    allFiles
  }

  def filesInDirectory(directory : File): Array[File] = {

    if(directory.isFile) {
      Array(directory)
    } else {

      val files = directory.listFiles

      files.map(file => {
          filesInDirectory(file)
      }).flatten
    }

  }

  def reachFromFileToRoot(files : List[File], root : File) : List[File] = {

    if(files.head.getParentFile != root) {
      reachFromFileToRoot(files.head.getParentFile :: files, root)
    } else {
      files
    }
  }

}
