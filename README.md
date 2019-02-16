# CoreProject
The main modules for faster start a project;


Use git command: ```git submodule add git://github.com/artemkopan/CoreProject.git base```

Add next configurations in settings.gradle:
```
include ':core-android',
        ':core-ui', ':core-app', ':core-mvvm',
        ':debug',
        ':debug-stetho-noop',
        ':design',
        ':image-loader',
        ':recycler', ':recycler-paging'

rootProject.children.each { project ->
    String relativeProjectPath = project.projectDir.path.replace(settingsDir.path, "")
    project.projectDir = new File("base/$relativeProjectPath")
}

//include app modules
include ':app'

```
