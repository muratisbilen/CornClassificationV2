import java.io.*;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Vector;

public class SerializeToDatabase {

    private static final String SQL_SERIALIZE_OBJECT = "INSERT INTO Projects(object_name, serialized_object) VALUES (?, ?)";
    private static final String SQL_DESERIALIZE_OBJECT = "SELECT serialized_object FROM Projects WHERE serialized_id = ?";

    public static long serializeJavaObjectToDB(Connection connection, Object objectToSerialize, Project originalObject) throws Exception {

        PreparedStatement pstmt = connection.prepareStatement(SQL_SERIALIZE_OBJECT);

        pstmt.setString(1, originalObject.getProjectName());
        pstmt.setObject(2, objectToSerialize);
        pstmt.executeUpdate();
        ResultSet rs = pstmt.getGeneratedKeys();
        int serialized_id = -1;
        if (rs.next()) {
            serialized_id = rs.getInt(1);
        }
        rs.close();
        pstmt.close();

        originalObject.setSerialized_id(serialized_id);
        return serialized_id;
    }

    public static String deSerializeJavaObjectFromDB(Connection connection, long serialized_id) throws SQLException, IOException, ClassNotFoundException {

        PreparedStatement pstmt = connection.prepareStatement(SQL_DESERIALIZE_OBJECT);
        pstmt.setLong(1, serialized_id);
        ResultSet rs = pstmt.executeQuery();
        rs.next();

        String deSerializedObject = (String)rs.getObject("serialized_object");
        rs.close();
        pstmt.close();

        return deSerializedObject;
    }
}