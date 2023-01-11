package scalaProjectFactory

import java.nio.file.{Files, OpenOption, Path, Paths, StandardOpenOption}
import scala.jdk.CollectionConverters.*

object ProjectFactory:
  /** Creates a new scala project with basic structure and Main function
   *
   * @param path path to folder in which the project will be created
   * @param projectName name of the project folder
   * @param mainPackageName name of the package
   */
  def createProject(path: String, projectName: String, mainPackageName: String): Unit =
    val projectDirName = s"$path/$projectName"
    val projectDirPath = Paths.get(projectDirName)
    Files.createDirectories(projectDirPath)

    val subDirPaths = List[String](
      "project",
      s"src/main/scala/$mainPackageName",
      s"src/test/scala/$mainPackageName")
    createSubDirs(projectDirName, subDirPaths)

    val resourcePath = "src/main/resources"
    val templatePathToDestPath = Map[String, String](
      ("build.sbt.template", "build.sbt"),
      ("build.properties.template", "project/build.properties"),
      ("Main.scala.template", s"src/main/scala/$mainPackageName/Main.scala")
    ).map((a, b) => (Paths.get(s"$resourcePath/$a"), Paths.get(s"$projectDirName/$b")))
    createFiles(templatePathToDestPath, mainPackageName)

  private def createSubDirs(mainDir: String, subDirPaths: List[String]): List[Path] =
    def createSubDir(subPath: String): Path =
      val path = Paths.get(s"$mainDir/$subPath")
      Files.createDirectories(path)
    end createSubDir

    subDirPaths.map(createSubDir)

  private def createFiles(templatePathToDestPath: Map[Path, Path], packageName: String): Unit =
    def readFile(path: Path): String =
      val lines = Files.readAllLines(path)
      val scalaLines = lines.asScala
      val scalaText = scalaLines.mkString("\n")
      scalaText

    def setPackageName(text: String, packageName: String): String =
      val replacedText = text.replace("packageName", packageName)
      replacedText

    def writeFile(path: Path, text: String) =
      Files.write(path, text.getBytes(), StandardOpenOption.CREATE_NEW)

    val content = templatePathToDestPath.map((template, dest) => (readFile(template), dest))
    content.map((text, dest) => writeFile(dest, setPackageName(text, packageName)))



