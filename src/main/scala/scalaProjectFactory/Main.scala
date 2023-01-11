package scalaProjectFactory

@main def run(path: String, projectName: String, packageName: String): Unit =
  ProjectFactory.createProject(path, projectName, packageName)