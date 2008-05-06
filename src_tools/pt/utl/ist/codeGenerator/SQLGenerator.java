package pt.utl.ist.codeGenerator;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Map;

import pt.utl.ist.codeGenerator.database.DatabaseDescriptorFactory;
import pt.utl.ist.codeGenerator.database.SqlTable;
import pt.ist.fenixframework.pstm.MetadataManager;

public class SQLGenerator {

    public static void main(String[] args) {
	try {
	    MetadataManager.init("build/WEB-INF/classes/domain_model.dml");
	    generate(args[0]);
	} catch (Exception ex) {
	    ex.printStackTrace();
	}

	System.out.println("Generation Complete.");
	System.exit(0);
    }

    private static void generate(final String destinationFilename) throws IOException {
	final StringBuilder stringBuilder = new StringBuilder();

	final Map<String, SqlTable> sqlTables = DatabaseDescriptorFactory.getSqlTables();
	for (final SqlTable sqlTable : sqlTables.values()) {
	    sqlTable.appendCreateTableMySql(stringBuilder);
	    stringBuilder.append("\n\n");
	}

	stringBuilder.append("insert into ROOT_DOMAIN_OBJECT (ID_INTERNAL) values (1);\n\n");
	writeFile(destinationFilename, stringBuilder.toString());
    }

    public static void writeFile(final String filename, final String fileContents) throws IOException {
	final File file = new File(filename);
	if (!file.exists()) {
	    file.createNewFile();
	}

	final FileWriter fileWriter = new FileWriter(file, false);

	fileWriter.write(fileContents);
	fileWriter.close();
    }

}
