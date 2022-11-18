package webapp.rating;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class App 
{

	public static void main(String[] args)
	{
		Connection conn = JDBCConnection.getConnection();
		try {
			conn.close();
		} catch (SQLException e) {
			System.out.println("Error while closing resources - " + e);
		}
	}

	static List<String> displayAssignmentCategory(Connection conn) {

		String sql = "SELECT STUDENTNAME, SUBJECT, ASSIGNMENTCATEGORY, DATE, MARKS FROM STUDENTS";
		List<String> sb = new ArrayList<String>();
		try {
			PreparedStatement st = conn.prepareStatement(sql);
			ResultSet rs = st.executeQuery();
			while(rs.next()) {
				sb.add(rs.getString(1)); sb.add(rs.getString(2)); sb.add(rs.getString(3)); sb.add(rs.getString(4)); sb.add(rs.getString(5));
				System.out.println();
			}
		} catch (SQLException e) {
			sb.add("Error! - " + e);
		}
		return sb;
	}
	@SuppressWarnings("resource")
	private static void validateAssignmentCategory(Connection conn, Assignments a) throws SQLException {

		String sql = "INSERT INTO STUDENTS VALUES (?,?,?,?,?)";
		PreparedStatement st = conn.prepareStatement(sql);
		st.setString(1, a.studentName);
		st.setString(2, a.subject);
		st.setString(3, a.assignmentCategory);
		st.setString(4, a.date);
		st.setInt(5, a.points);
		st.executeUpdate();
		int seq = 0;
		if(a.assignmentCategory.equalsIgnoreCase("test")){
			try {
				sql = "INSERT INTO TEST (STUDENTNAME, SUBJECT, ASSIGNMENTCATEGORY, MARKS) VALUES (?, ?, ?, ?) ON DUPLICATE KEY UPDATE SEQUENCE = TEST.SEQUENCE + 1"
						+ ", MARKS = TEST.MARKS + ?";

				st = conn.prepareStatement(sql);
				st.setString(1, a.getStudentName());
				st.setString(2, a.getSubject());
				st.setString(3, a.getAssignmentCategory());
				st.setInt(4, a.getPoints());
				st.setInt(5, a.getPoints());
				st.executeUpdate();
				sql = "SELECT SEQUENCE FROM TEST WHERE STUDENTNAME = ?  AND SUBJECT = ? AND ASSIGNMENTCATEGORY = ?";
				st = conn.prepareStatement(sql);
				st.setString(1, a.getStudentName());
				st.setString(2, a.getSubject());
				st.setString(3, a.getAssignmentCategory());
				ResultSet rs = st.executeQuery();
				while(rs.next()) {
					seq = rs.getInt(1);
				}
				sql = "INSERT INTO OVERALLRATING (STUDENTNAME, SUBJECT, OVERALLSCORE, TESTSCORE) SELECT * FROM (SELECT STUDENTNAME, SUBJECT, (0.4 * (SUM(MARKS) / SEQUENCE)) AS TOTAL, "
						+ "(0.4 * (SUM(MARKS) / SEQUENCE)) FROM TEST WHERE STUDENTNAME = ? AND SUBJECT = ?) AS T ON duplicate key update OVERALLSCORE = OVERALLSCORE - TESTSCORE, TESTSCORE = T.TOTAL, OVERALLSCORE = OVERALLSCORE + TESTSCORE";
				st = conn.prepareStatement(sql);
				st.setString(1, a.getStudentName());
				st.setString(2, a.getSubject());
				st.executeUpdate();
			}
			catch (SQLException e) {
				System.out.println("Error - TEST" + e);

			}
		}

		else if(a.assignmentCategory.equalsIgnoreCase("quiz")){
			try {

				sql = "INSERT INTO QUIZ (STUDENTNAME, SUBJECT, ASSIGNMENTCATEGORY, MARKS) VALUES (?, ?, ?, ?) ON DUPLICATE KEY UPDATE SEQUENCE = QUIZ.SEQUENCE + 1"
						+ ", MARKS = QUIZ.MARKS + ?";

				st = conn.prepareStatement(sql);
				st.setString(1, a.getStudentName());
				st.setString(2, a.getSubject());
				st.setString(3, a.getAssignmentCategory());
				st.setInt(4, a.getPoints());
				st.setInt(5, a.getPoints());
				st.executeUpdate();
				sql = "SELECT SEQUENCE FROM QUIZ WHERE STUDENTNAME = ?  AND SUBJECT = ? AND ASSIGNMENTCATEGORY = ?";
				st = conn.prepareStatement(sql);
				st.setString(1, a.getStudentName());
				st.setString(2, a.getSubject());
				st.setString(3, a.getAssignmentCategory());
				ResultSet rs = st.executeQuery();
				while(rs.next()) {
					seq = rs.getInt(1);
				}
				sql = "INSERT INTO OVERALLRATING (STUDENTNAME, SUBJECT, OVERALLSCORE, QUIZSCORE) SELECT * FROM (SELECT STUDENTNAME, SUBJECT, (0.2 * (SUM(MARKS) / SEQUENCE)) AS TOTAL, "
						+ "(0.2 * (SUM(MARKS) / SEQUENCE)) FROM QUIZ WHERE STUDENTNAME = ? AND SUBJECT = ?) AS T ON duplicate key update OVERALLSCORE = OVERALLSCORE - QUIZSCORE, QUIZSCORE = T.TOTAL, OVERALLSCORE = OVERALLSCORE + QUIZSCORE";
				st = conn.prepareStatement(sql);
				st.setString(1, a.getStudentName());
				st.setString(2, a.getSubject());
				st.executeUpdate();
			} catch (SQLException e) {
				System.out.println("Error - QUIZ" + e);
			}
		}
		else if(a.assignmentCategory.equalsIgnoreCase("lab")){
			try {
				sql = "INSERT INTO LAB (STUDENTNAME, SUBJECT, ASSIGNMENTCATEGORY, MARKS) VALUES (?, ?, ?, ?) ON DUPLICATE KEY UPDATE SEQUENCE = LAB.SEQUENCE + 1"
						+ ", MARKS = LAB.MARKS + ?";

				st = conn.prepareStatement(sql);
				st.setString(1, a.getStudentName());
				st.setString(2, a.getSubject());
				st.setString(3, a.getAssignmentCategory());
				st.setInt(4, a.getPoints());
				st.setInt(5, a.getPoints());
				st.executeUpdate();
				sql = "SELECT SEQUENCE FROM LAB WHERE STUDENTNAME = ?  AND SUBJECT = ? AND ASSIGNMENTCATEGORY = ?";
				st = conn.prepareStatement(sql);
				st.setString(1, a.getStudentName());
				st.setString(2, a.getSubject());
				st.setString(3, a.getAssignmentCategory());
				ResultSet rs = st.executeQuery();
				while(rs.next()) {
					seq = rs.getInt(1);
				}
				sql = "INSERT INTO OVERALLRATING (STUDENTNAME, SUBJECT, OVERALLSCORE, LABSCORE) SELECT * FROM (SELECT STUDENTNAME, SUBJECT, (0.1 * (SUM(MARKS) / SEQUENCE)) AS TOTAL, "
						+ "(0.1 * (SUM(MARKS) / SEQUENCE)) FROM LAB WHERE STUDENTNAME = ? AND SUBJECT = ?) AS T ON duplicate key update OVERALLSCORE = OVERALLSCORE - LABSCORE, LABSCORE = T.TOTAL, OVERALLSCORE = OVERALLSCORE + LABSCORE";
				st = conn.prepareStatement(sql);
				st.setString(1, a.getStudentName());
				st.setString(2, a.getSubject());
				st.executeUpdate();
			} catch (SQLException e) {
				System.out.println("Error - LAB" + e);
			}
		}
		else if(a.assignmentCategory.equalsIgnoreCase("project")){
			try {

				sql = "INSERT INTO PROJECT (STUDENTNAME, SUBJECT, ASSIGNMENTCATEGORY, MARKS) VALUES (?, ?, ?, ?) ON DUPLICATE KEY UPDATE SEQUENCE = PROJECT.SEQUENCE + 1"
						+ ", MARKS = PROJECT.MARKS + ?";

				st = conn.prepareStatement(sql);
				st.setString(1, a.getStudentName());
				st.setString(2, a.getSubject());
				st.setString(3, a.getAssignmentCategory());
				st.setInt(4, a.getPoints());
				st.setInt(5, a.getPoints());
				st.executeUpdate();
				sql = "SELECT SEQUENCE FROM PROJECT WHERE STUDENTNAME = ?  AND SUBJECT = ? AND ASSIGNMENTCATEGORY = ?";
				st = conn.prepareStatement(sql);
				st.setString(1, a.getStudentName());
				st.setString(2, a.getSubject());
				st.setString(3, a.getAssignmentCategory());
				ResultSet rs = st.executeQuery();
				while(rs.next()) {
					seq = rs.getInt(1);
				}
				sql = "INSERT INTO OVERALLRATING (STUDENTNAME, SUBJECT, OVERALLSCORE, PROJECTSCORE) SELECT * FROM (SELECT STUDENTNAME, SUBJECT, (0.3 * (SUM(MARKS) / SEQUENCE)) AS TOTAL, "
						+ "(0.3 * (SUM(MARKS) / SEQUENCE)) FROM PROJECT WHERE STUDENTNAME = ? AND SUBJECT = ?) AS T ON duplicate key update OVERALLSCORE = OVERALLSCORE - PROJECTSCORE, PROJECTSCORE = T.TOTAL, OVERALLSCORE = OVERALLSCORE + PROJECTSCORE";
				st = conn.prepareStatement(sql);
				st.setString(1, a.getStudentName());
				st.setString(2, a.getSubject());
				st.executeUpdate();	
			} catch (SQLException e) {
				System.out.println("Error - PROJECT" + e);
			}
		}
		else {
			System.out.println("Please enter valid assignment category.");
		}

		if(seq != 0) {
			sql = "UPDATE STUDENTS SET ASSIGNMENTCATEGORY = ? WHERE STUDENTNAME = ?  AND SUBJECT = ? AND ASSIGNMENTCATEGORY = ?"; 
			try {
				st = conn.prepareStatement(sql);
				String category = a.getAssignmentCategory() + "_" + seq;
				st.setString(1, category);
				st.setString(2, a.getStudentName());
				st.setString(3, a.getSubject());
				st.setString(4, a.getAssignmentCategory());
				st.executeUpdate();
			} catch (SQLException e) {
				System.out.println("Error - UPDATE" + e);
			}
		}
		else {
			System.out.println("Error.Invalid assignment category");
		}
	}

	@SuppressWarnings("resource")
	static String removeAssignmentCategory(Connection conn, String name1, String subject1, String assigncategory1) {

		try {
			String sql = "SELECT MARKS FROM STUDENTS WHERE STUDENTNAME = ? AND SUBJECT = ? AND ASSIGNMENTCATEGORY = ?";
			PreparedStatement st = conn.prepareStatement(sql);
			st.setString(1, name1);
			st.setString(2, subject1);
			st.setString(3, assigncategory1);
			ResultSet rs = st.executeQuery();
			int marks = 0, n = 0;
			while(rs.next()) {
				marks = rs.getInt(1);
			}
			if(assigncategory1.substring(0, 4).equalsIgnoreCase("test")) {
				sql = "SELECT SEQUENCE FROM TEST WHERE STUDENTNAME = ? AND SUBJECT = ?";
				st = conn.prepareStatement(sql);
				st.setString(1, name1);
				st.setString(2, subject1);
				rs = st.executeQuery();
				int seq = 0;
				while(rs.next()) {
					seq = rs.getInt(1);
				}
				sql = "UPDATE OVERALLRATING SET OVERALLSCORE = OVERALLSCORE - TESTSCORE, TESTSCORE = ((TESTSCORE * ?) - (? * 0.4)) / (? - 1), OVERALLSCORE = OVERALLSCORE + TESTSCORE  WHERE STUDENTNAME = ? AND SUBJECT = ?";
				st = conn.prepareStatement(sql);
				st.setInt(1, seq);
				st.setInt(2, marks);
				st.setInt(3, seq);
				st.setString(4, name1);
				st.setString(5, subject1);
				st.executeUpdate();
				sql = "UPDATE TEST SET MARKS = MARKS - ?, SEQUENCE = SEQUENCE - 1 WHERE STUDENTNAME = ? AND SUBJECT = ?";
				st = conn.prepareStatement(sql);
				st.setInt(1, marks);
				st.setString(2, name1);
				st.setString(3, subject1);
				n = st.executeUpdate();

			}
			else if(assigncategory1.substring(0, 4).equalsIgnoreCase("quiz")) {
				sql = "SELECT SEQUENCE FROM QUIZ WHERE STUDENTNAME = ? AND SUBJECT = ?";
				st = conn.prepareStatement(sql);
				st.setString(1, name1);
				st.setString(2, subject1);
				st.executeQuery();
				int seq = 0;
				while(rs.next()) {
					seq = rs.getInt(1);
				}
				sql = "UPDATE OVERALLRATING SET QUIZSCORE = ((QUIZSCORE * ?) - (? * 0.2)) / (? - 1), OVERALLSCORE = (TESTSCORE + QUIZSCORE + LABSCORE + PROJECTSCORE) WHERE STUDENTNAME = ? AND SUBJECT = ?";
				st = conn.prepareStatement(sql);
				st.setInt(1, seq);
				st.setInt(2, marks);
				st.setInt(3, seq);
				st.setString(4, name1);
				st.setString(5, subject1);
				st.executeUpdate();
				sql = "UPDATE QUIZ SET MARKS = MARKS - ?, SEQUENCE = SEQUENCE - 1 WHERE STUDENTNAME = ? AND SUBJECT = ?";
				st = conn.prepareStatement(sql);
				st.setInt(1, marks);
				st.setString(2, name1);
				st.setString(3, subject1);
				n = st.executeUpdate();
			}
			else if(assigncategory1.substring(0, 3).equalsIgnoreCase("lab")) {
				sql = "SELECT SEQUENCE FROM LAB WHERE STUDENTNAME = ? AND SUBJECT = ?";
				st = conn.prepareStatement(sql);
				st.setString(1, name1);
				st.setString(2, subject1);
				st.executeQuery();
				int seq = 0;
				while(rs.next()) {
					seq = rs.getInt(1);
				}
				sql = "UPDATE OVERALLRATING SET LABSCORE = ((LABSCORE * ?) - (? * 0.1)) / (? - 1), OVERALLSCORE = (TESTSCORE + QUIZSCORE + LABSCORE + PROJECTSCORE) WHERE STUDENTNAME = ? AND SUBJECT = ?";
				st = conn.prepareStatement(sql);
				st.setInt(1, seq);
				st.setInt(2, marks);
				st.setInt(3, seq);
				st.setString(4, name1);
				st.setString(5, subject1);
				st.executeUpdate();
				sql = "UPDATE LAB SET MARKS = MARKS - ?, SEQUENCE = SEQUENCE - 1 WHERE STUDENTNAME = ? AND SUBJECT = ?";
				st = conn.prepareStatement(sql);
				st.setInt(1, marks);
				st.setString(2, name1);
				st.setString(3, subject1);
				n = st.executeUpdate();
			}
			else if(assigncategory1.substring(0, 4).equalsIgnoreCase("proj")) {
				sql = "SELECT SEQUENCE FROM PROJECT WHERE STUDENTNAME = ? AND SUBJECT = ?";
				st = conn.prepareStatement(sql);
				st.setString(1, name1);
				st.setString(2, subject1);
				st.executeQuery();
				int seq = 0;
				while(rs.next()) {
					seq = rs.getInt(1);
				}
				sql = "UPDATE OVERALLRATING SET PROJECTSCORE = ((PROJECTSCORE * ?) - (? * 0.3)) / (? - 1), OVERALLSCORE = (TESTSCORE + QUIZSCORE + LABSCORE + PROJECTSCORE) WHERE STUDENTNAME = ? AND SUBJECT = ?";
				st = conn.prepareStatement(sql);
				st.setInt(1, seq);
				st.setInt(2, marks);
				st.setInt(3, seq);
				st.setString(4, name1);
				st.setString(5, subject1);
				st.executeUpdate();
				sql = "UPDATE PROJECT SET MARKS = MARKS - ?, SEQUENCE = SEQUENCE - 1 WHERE STUDENTNAME = ? AND SUBJECT = ?";
				st = conn.prepareStatement(sql);
				st.setInt(1, marks);
				st.setString(2, name1);
				st.setString(3, subject1);
				n = st.executeUpdate();
			}
			else {
				return "Please enter valid assignment category.";
			}
			if(n != 0) {
				sql = "DELETE FROM STUDENTS WHERE STUDENTNAME = ? AND SUBJECT = ? AND ASSIGNMENTCATEGORY = ?";
				st = conn.prepareStatement(sql);
				st.setString(1, name1);
				st.setString(2, subject1);
				st.setString(3, assigncategory1);
				n = st.executeUpdate();
				if(n == 1) {
					return "Assignment Category deleted successfully!";
				}
			}
		}catch(SQLException e) {
			return "Error! - " + e;
		}
		return "Error!";
	}

	static String addAssignmentCategory(Connection conn, String name, String subject, String assigncategory, String date, int marks) {

		Assignments a = new Assignments(name, subject, assigncategory, date, marks);  
		try {
			validateAssignmentCategory(conn, a);
			return "Assignment category inserted successfully!";
		} catch (SQLException e) {
			return "Data insertion failed! Error - " + e;
		}

	}

	static List<String> displaySubjectWise(Connection conn, String subjectName) {

		String sql = "SELECT * FROM OVERALLRATING WHERE SUBJECT = ?";
		List<String> sb = new ArrayList<String>();
		PreparedStatement st;
		try {
			st = conn.prepareStatement(sql);
			st.setString(1, subjectName);
			sb.add(subjectName);
			ResultSet rs = st.executeQuery();
			while(rs.next()) {
				sb.add(rs.getString(1)); sb.add((rs.getFloat(3) != 0)? rs.getString(3) : "NA"); 
				sb.add((rs.getFloat(4) != 0) ? rs.getString(4) : "NA"); sb.add((rs.getFloat(5) != 0) ? rs.getString(5) : "NA");
				sb.add((rs.getFloat(6) != 0) ? rs.getString(6) : "NA"); sb.add((rs.getFloat(7) != 0) ? rs.getString(7) : "NA");
				
			}
		} catch (SQLException e) {
			
			sb.add("Error! - " + e);
		}
		return sb;

	}

	static List<String> displayStudentWise(Connection conn, String studentName) {

		String sql = "SELECT * FROM OVERALLRATING WHERE STUDENTNAME = ?";
		List<String> sb = new ArrayList<String>();
		PreparedStatement st;
		try {
			st = conn.prepareStatement(sql);
			st.setString(1, studentName);
			ResultSet rs = st.executeQuery();
		
			sb.add(studentName);
			
			while(rs.next()) {
			
				sb.add(rs.getString(2)); sb.add((rs.getFloat(3) != 0)? rs.getString(3) : "NA"); 
				sb.add((rs.getFloat(4) != 0) ? rs.getString(4) : "NA"); sb.add((rs.getFloat(5) != 0) ? rs.getString(5) : "NA");
				sb.add((rs.getFloat(6) != 0) ? rs.getString(6) : "NA"); sb.add((rs.getFloat(7) != 0) ? rs.getString(7) : "NA");
				
			}

		} catch (SQLException e) {
			
			sb.add("Error - " + e);
		}
		return sb;
	}

	static List<String> viewData(Connection conn, String sname) {

		String sql = "SELECT * FROM STUDENTS WHERE STUDENTNAME = ?";
		List<String> sb = new ArrayList<String>();
		try {
			PreparedStatement st = conn.prepareStatement(sql);
			st.setString(1, sname);
			ResultSet rs = st.executeQuery();

			
			sb.add(sname);
		
			while(rs.next()){
	
				sb.add(rs.getString(2)); sb.add(rs.getString(3)); sb.add(rs.getString(4)); sb.add(rs.getString(5));
		
			}
		} catch (SQLException e) {
			sb.add("Error! - " + e);
		
		}
		sb = Collections.synchronizedList(sb);  
		return sb;
	}

	static String deleteData(Connection conn, String na) {

		String sql = "DELETE FROM STUDENTS WHERE STUDENTNAME = ?";
		try {
			PreparedStatement st = conn.prepareStatement(sql);
			st.setString(1, na);
			int n = st.executeUpdate();
			if(n != 0) {
				sql = "DELETE FROM OVERALLRATING WHERE STUDENTNAME = ?";
				st = conn.prepareStatement(sql);
				st.setString(1, na);
				st.executeUpdate();
				return "Data deleted successfully!";
			}
			else {
				return "Data deletion failed!";
			}
		} catch (SQLException e) {
			return "Error! - " + e;
		}
	}

	static String addStudentData(Connection conn, String n, String sub, String category, String date, int marks) {

		try {
			Assignments a = new Assignments(n, sub, category, date, marks);
			validateAssignmentCategory(conn, a);
			return "Data inserted successfully!";
		}
		catch(Exception e){
			return "Data cannot be inserted! " + e;
		}

	}

}

