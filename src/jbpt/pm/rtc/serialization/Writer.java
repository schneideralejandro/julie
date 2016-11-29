package jbpt.pm.rtc.serialization;

import jbpt.pm.rtc.structure.PM;
import org.jbpt.pm.ProcessModel;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.util.Collection;

public class Writer {
  private PMtoProcessModel pmToProcessModel = null;
  private Collection<Serializer> serializers = null;
  private Path directory = null;

  public Writer(Collection<Serializer> s) {
    pmToProcessModel = new PMtoProcessModel();
    serializers = s;
    directory = createDirectory();
  }

  public Path createDirectory() {
    Path directory = null;
    String currentWorkingDirectory = System.getProperty("user.dir");
    String date = LocalDateTime.now().toString();
    String path = currentWorkingDirectory;
    path += File.separator + "generated-models";
    path += File.separator + "date-" + date;
    try {
      directory = Files.createDirectories(Paths.get(path));
    } catch (IOException e) {
      String error = "";
      error += "IOException: " + e + "\n";
      error += "serializer.dir will remain as null.\n";
      error += "JSON files will be stored at: ";
      error += currentWorkingDirectory + "\n\n";
      System.err.print(error);
    }
    return directory;
  }

  public void write(PM pm) {
    ProcessModel processModel = pmToProcessModel.serialize(pm);
    for (Serializer serializer: serializers) {
      serializer.serialize(processModel, directory);
    }
  }

  public void write(Collection<PM> models) {
    for (PM model: models) {
      write(model);
    }
  }
}
