package edu.knoldus.filer

import java.io.{File, FileNotFoundException, IOException}

import scala.concurrent.Future
import scala.concurrent.ExecutionContext.Implicits.global

/**
  * Created by ashish on 2/2/17.
  */

class FileLister {

  @throws[FileNotFoundException]
  @throws[IOException]
  @throws[IndexOutOfBoundsException]
  def listFiles(directory : String): Future[List[String]] = {

    val classLoader:ClassLoader = getClass.getClassLoader
    val rootDirectory:File = new File(classLoader.getResource(directory).getFile)

    val listOfFiles:Future[List[File]] = Future{ filesInDirectory(rootDirectory) }

    val finalList = listOfFiles.map(_.map(file => reachFromFileToRoot(List[File](file), rootDirectory)))

    getListInString(finalList)
  }

  def getListInString(listOfFiles: Future[List[List[File]]]): Future[List[String]] = {
    listOfFiles.map(list => {
      list.map(innerList => {
        stringify(innerList)
      })
    })
  }

  def stringify(list:List[File], pointer:Int = 0, result:String = ""): String = {
    if(pointer < list.length) {
      result + list(pointer).getName + "/" + stringify(list, pointer + 1)
    }
    else {
      result
    }
  }

  private def filesInDirectory(directory : File): List[File] = {

    if(directory.isFile) {
      List(directory)
    } else {

      val files:List[File] = directory.listFiles.toList

      files.map(file => {
          filesInDirectory(file)
      }).flatten
    }

  }

  private def reachFromFileToRoot(files : List[File], root : File) : List[File] = {

    if(files.head.getParentFile != root) {
      reachFromFileToRoot(files.head.getParentFile :: files, root)
    } else {
      files
    }
  }

}
