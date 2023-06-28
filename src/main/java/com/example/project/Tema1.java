package com.example.project;
import java.io.*;
import java.util.Arrays;

public class Tema1 {

	static int id_questions = 1, id_quiz = 1, id_answers = 1;
	String user, pass;

	// method that verifies if a user and password are correct and provided
	public int CredentialsVerification (final String args[]){

		if (args.length < 3) {
			System.out.print("{ 'status' : 'error', 'message' : 'You need to be authenticated'}");
			return 0;
		}

		String value = args[1];
		this.user = value.replaceAll("-u", "");

		value = args[2];
		this.pass = value.replaceAll("-p", "");

		SearchForUSer search = new SearchForUSer();
		if (search.UserExist(this.user, this.pass) == 0) {
			System.out.print("{ 'status' : 'error', 'message' : 'Login failed'}");
			return 0;
		}

		return 1;
	}
	public static class Delete {

		// method that deletes the data files
		public void DeletingFiles() {

			File file = new File("users.csv");
			if (file.delete()) {
				System.out.print("{ 'status' : 'ok', 'message' : 'Cleanup finished successfully'}");
			}

			file = new File("questions.csv");
			if (file.delete()) {
				System.out.print("{ 'status' : 'ok', 'message' : 'Cleanup finished successfully'}");
			}

			file = new File("quiz.csv");
			if (file.delete()) {
				System.out.print("{ 'status' : 'ok', 'message' : 'Cleanup finished successfully'}");
			}
		}
	}
	public static class SearchForText {

		// method that searches if a text already exists in the questions file
		public String TextExist(String text) {

			try (BufferedReader br = new BufferedReader(new FileReader(("questions.csv")))) {
				String line;

				while ((line = br.readLine()) != null) {
					String[] values = line.split(",", 4);

					// if the text was found we return the id of the question
					if (values[3].contains(text)) {
						return values[0];
					}
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
			return "-1";
		}
	}
	public static class SearchForUSer {

		// method that searches if a user and password combination exists
		public int UserExist(String user, String password) {

			try (BufferedReader br = new BufferedReader(new FileReader(("users.csv")))) {
				String line;
				while ((line = br.readLine()) != null) {
					String[] values = line.split(",", 2);

					if (values[0].contains(user)) {

						// if the user is found and the password matches
						if(values[1].contains(password) == false ) {
							return 0;
						} else {
							return 1;
						}

					}
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
			return 0;
		}
	}
	public static class Questions {

		private String text, user, password, type;

		// method that verifies if there are 2 answers that have the same text
		public int IdenticalAnswers(final String args[]) {

				for (int i = 5; i < args.length - 3; i += 2) {
					for (int j = i + 2; j < args.length - 1; j += 2) {
						if (args[i].substring(10).contains(args[j].substring(10))) {
							return 0;
						}
					}
				}
			return 1;
		}

		// method that verifies if a single type question has many correct answers
		public int SingelHasManyYesAnswers (final String args[]) {
			int correct = 0;

			for (int i = 7 ; i <= args.length; i+=2) {
				if(args[i - 1].contains("1"))
					correct++;
			}

			if (correct > 1) return 0;
			return 1;

		}

		// method that verifies if every answer has a text and a "is correct" value
		public int WrongAnswers(final String args[]) {

			int contor = 1;
			for (int i = 6; i <= args.length; i+=2) {

				if(args[i - 1].contains("-answer-"+Integer.toString(contor)+"-is-correct")){
					System.out.print("{ 'status' : 'error', 'message' : 'Answer " + Integer.toString(contor) + " has no answer description'}");
					return 0;
				} else if (!args[i].contains("-answer-"+Integer.toString(contor)+"-is-correct")) {
					System.out.print("{ 'status' : 'error', 'message' : 'Answer " + Integer.toString(contor) + " has no answer correct flag'}");
					return 0;
				}

				contor++;
			}
			return 1;
		}

		// inserting a question in the questions file
		public int InsertQuestion(final String args[]) {

			String value = args[4];
			this.type = value.replaceAll("-type", "");

			try (FileWriter fw = new FileWriter("questions.csv", true);
				 BufferedWriter bw = new BufferedWriter(fw);
				 PrintWriter out = new PrintWriter(bw)) {

				out.print(id_questions + "," + this.user + "," + this.password + "," + this.text + "," + this.type + ",");

				int contor = 1;
				for (int i = 7; i <= args.length; i+=2) {
					out.print(args[i - 2].replaceAll("-answer-" +
							Integer.toString(contor), "") + "@" +
							args[i - 1].replaceAll("-answer-" +
							Integer.toString(contor) + "-is-correct ", "") + "@" + id_answers + ",");

					contor++;
					id_answers++;
				}
				out.println();
				System.out.print("{ 'status' : 'ok', 'message' : 'Question added successfully'}");

			} catch (IOException e) {
				e.printStackTrace();
			}
			return 1;
		}
		public void VerifyUsernameQuestion(final String[] args) {

			if(args.length >= 3) {
				if(args[1].contains("-u") == false || args[2].contains("-p") == false) {
					System.out.print("{ 'status' : 'error', 'message' : 'You need to be authenticated'}");
					return;
				}
			} else {
				System.out.print("{ 'status' : 'error', 'message' : 'You need to be authenticated'}");
				return;
			}

			String value = args[1];
			this.user = value.replaceAll("-u", "");

			value = args[2];
			this.password = value.replaceAll("-p", "");

			SearchForUSer search = new SearchForUSer();
			if(search.UserExist(this.user, this.password) == 0) {
				System.out.print("{ 'status' : 'error', 'message' : 'Login failed'}");
				return;
			}

			if (args[3].contains("-text") == false) {
				System.out.print("{ 'status' : 'error', 'message' : 'No question text provided'}");
				return;
			}

			value = args[3];
			this.text = value.replaceAll("-text", "");

			SearchForText searchText = new SearchForText();
			if (searchText.TextExist(this.text) != "-1") {
				System.out.print("{ 'status' : 'error', 'message' : 'Question already exists'}");
				return;
			}

			if(args.length <= 5) {
				System.out.print("{ 'status' : 'error', 'message' : 'No answer provided'}");
				return;
			}

			if(args.length == 7) {
				System.out.print("{ 'status' : 'error', 'message' : 'Only one answer provided'}");
				return;
			} else if (args.length > 15) {
				System.out.print("{ 'status' : 'error', 'message' : 'More than 5 answers were submitted'}");
				return;
			}

			if(this.SingelHasManyYesAnswers(args) == 0) {
				System.out.print("{ 'status' : 'error', 'message' : 'Single correct answer question has more than one correct answer'}");
				return;
			}

			if(this.IdenticalAnswers(args) == 0) {
				System.out.print("{ 'status' : 'error', 'message' : 'Same answer provided more than once'}");
				return;
			}

			if(this.WrongAnswers(args) == 0)
				return;

			if(this.InsertQuestion(args) == 1)
				id_questions++;
		}
	}
	public static class UserCommands {
		private String password, username;
		public void VerifyUsername(final String[] args) {

			if (args.length > 1) {
				String value = args[1];
				this.username = value.replaceAll("-u", "");

				value = args[1];
				this.password = value.replaceAll("-p", "");

				if (args.length < 3) {  // password was not provided
					System.out.print("{'status':'error','message':'Please provide password'}");
					return;
				}

				SearchForUSer search = new SearchForUSer();
				if(search.UserExist(this.username, this.password) == 1) {
					System.out.print("{ 'status' : 'error', 'message' : 'User already exists'}");
					return;
				}

				this.VerifyPassword(args);

			} else {
				System.out.print("{'status':'error','message':'Please provide username'}");
			}
		}

		// method that verifies the password and stores the user in the file
		public void VerifyPassword(final String[] args) {
			if (args.length == 3 && args[2].contains("-p")) {

				try (FileWriter fw = new FileWriter("users.csv", true);
					 BufferedWriter bw = new BufferedWriter(fw);
					 PrintWriter out = new PrintWriter(bw)) {

					out.println(this.username + "," + this.password);
					System.out.print("{ 'status' : 'ok', 'message' : 'User created successfully'}");

				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}
	public static class GetQuestion {

		String text;
		public void Verify(final String args[]){

			Tema1 verif = new Tema1();
			verif.CredentialsVerification(args);

			if (args.length < 4) return;

			String value = args[3];
			this.text = value.replaceAll("-text", "");

			SearchForText searchText = new SearchForText();
			String result = searchText.TextExist(this.text);

			// if the method returns -1 the line for the quiz does not exist in the file
			if (result == "-1") {
				System.out.print("{ 'status' : 'error', 'message' : 'Question does not exist'}");
				return;
			} else {
				System.out.print("{ 'status' : 'ok', 'message' : '"+ result + "'}");
			}
		}
	}

	public static class GetAllQuestions {

		String username, password;
		static int contor = 0;

		public void All(String username) {

			try (BufferedReader br = new BufferedReader(new FileReader(("questions.csv")))) {
				String line;

				while ((line = br.readLine()) != null) {
					contor++;

					// the contor is used for placing the coma in the iteration
					String[] values = line.split(",", 10);
					if (values[1].contains(username)) {
							if(contor == 1)
								System.out.print("{\"question_id\" : \""+ values[0] + "\", \"question_name\" : \""+
										values[3].substring(2, values[3].length() - 1) +"\"}");

							if(contor != 1)
								System.out.print(", {\"question_id\" : \""+ values[0] + "\", \"question_name\" : \""+
										values[3].substring(2, values[3].length() - 1) +"\"}]' ");
					}

				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}

		public void VerifyAll(final String args[]) {

			if (args.length < 3) {
				System.out.print("{ 'status' : 'error', 'message' : 'You need to be authenticated'}");
				return;
			}

			String value = args[1];
			this.username = value.replaceAll("-u", "");

			value = args[2];
			this.password = value.replaceAll("-p", "");

			SearchForUSer search = new SearchForUSer();
			if (search.UserExist(this.username, this.password) == 0) {
				System.out.print("{ 'status' : 'error', 'message' : 'Login failed'}");
				return;
			}

			System.out.print("{ 'status' : 'ok', 'message':'[");
			this.All(this.username);
			System.out.print("}");

		}
	}

	public static class CreateQuiz {

		String username, password, quizName;

		// method that verifies if a quiz with the specified name exists
		public int NameExist(String name) {
			try (BufferedReader br = new BufferedReader(new FileReader("quiz.csv"))) {
				String line;
				while ((line = br.readLine()) != null) {

					if (line.contains(name)) {
							return 0;
					}
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
			return 1;
		}

		// the quiz is inserted in the file
		public void InsertQuiz(final String args[]) {

			try (FileWriter fw = new FileWriter("quiz.csv", true);
				 BufferedWriter bw = new BufferedWriter(fw);
				 PrintWriter out = new PrintWriter(bw)) {

				out.print(this.username + "," + this.password + "," + this.quizName + ",");

				for (int i = 4; i < args.length; i++) {
					int questionNumber = i - 3;
					out.print(args[i].replaceAll("-question-" + Integer.toString(questionNumber), "") + ",");
				}

				// we print 0 because the quiz is not completed
				out.println(id_quiz + "," + 0);

				System.out.print("{ 'status' : 'ok', 'message' : 'Quizz added succesfully'}");

			} catch (IOException e) {
				e.printStackTrace();
			}
		}

		public void Verify (final String args[]) {

			Tema1 verif = new Tema1();
			if (verif.CredentialsVerification(args) == 0)
				return;

			String value = args[1];
			this.username = value.replaceAll("-u", "");

			value = args[2];
			this.password = value.replaceAll("-p", "");

			if (args.length > 14) {
				System.out.print("{ 'status' : 'error', 'message' : 'Quizz has more than 10 questions'}");
				return;
			}

			if (args.length < 4)
				return;

			value = args[3];
			this.quizName = value.replaceAll("-name", "");

			if (this.NameExist(this.quizName) == 0) {
				System.out.print("{ 'status' : 'error', 'message' : 'Quizz name already exists'}");
				return;
			}

			// search for the id in the questions file for each question of the quiz
			for (int i = 4; i < args.length; i++) {

				String id = args[i].replace("-question-", "");
				id = id.substring(3, id.length() - 1);

				if (Integer.parseInt(id) > id_questions - 1) {
					System.out.print("{ 'status' : 'error', 'message' : 'Question ID for question " + id + " does not exist'}");
					return;
				}
			}
			this.InsertQuiz(args);
			id_quiz++;
		}
	}

	public static class GetQuiz {

		String quizName;

		// method that searches for a quiz and returns the id
		public String SearchQuizId(String name) {
			try (BufferedReader br = new BufferedReader(new FileReader("quiz.csv"))) {
				String line;

				while ((line = br.readLine()) != null) {

					String[] values = line.split(",", 7);

					if (line.contains(name)) {
						return values[5];
					}
				}

			} catch (IOException e) {
				e.printStackTrace();
			}
			return "0";
		}

		public void Verify(final String args[]) {

			Tema1 verif = new Tema1();
			verif.CredentialsVerification(args);

			if (args.length < 4)
				return;

			String value = args[3];
			this.quizName = value.replaceAll("-name ", "");

			CreateQuiz q = new CreateQuiz();
			if (q.NameExist(this.quizName) == 1) {
				System.out.print("{ 'status' : 'error', 'message' : 'Quizz does not exist'}");
				return;
			}

			String id = this.SearchQuizId(quizName);
			if (!id.equals("0")) {
				System.out.print( "{ 'status' : 'ok', 'message' : '" + id + "'}");
			}
		}
	}

	public static class GetAllQuiz {

		String username;

		public void SearchForQuiz(String username) {
			try (BufferedReader br = new BufferedReader(new FileReader(("quiz.csv")))) {
				String line;

				// the contor variable is used for the placement of the coma
				int contor = 1;
				while ((line = br.readLine()) != null) {
					String[] values = line.split(",", 7);

					// if the text was found we return the id of the question
					if (values[0].contains(username)) {
						int value = Integer.parseInt(values[6]);

						if(contor == 1 && value == 0)
							System.out.print("{\"quizz_id\" : \""+ values[5] +"\", \"quizz_name\" : \""+
									values[2] .substring(2, values[2].length() - 1)+ "\", \"is_completed\" : \"False\"}");

						if(contor == 2 && value == 0)
							System.out.print(", {\"quizz_id\" : \""+ values[5] +"\", \"quizz_name\" : \""+
									values[2].substring(2, values[2].length() - 1) + "\", \"is_completed\" : \"False\"}");

						// if the value is 0 the quiz was not completed, if it is 1 it was completed
						if(contor == 1 && value == 1)
							System.out.print("{\"quizz_id\" : \""+ values[5] +"\", \"quizz_name\" : \""+
									values[2] .substring(2, values[2].length() - 1)+ "\", \"is_completed\" : \"True\"}");

						if(contor == 2 && value == 1)
							System.out.print(", {\"quizz_id\" : \""+ values[5] +"\", \"quizz_name\" : \""+
									values[2].substring(2, values[2].length() - 1) + "\", \"is_completed\" : \"True\"}");

						contor++;
					}
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}

		public void VerifyAll(final String args[]) {

			Tema1 verif = new Tema1();
			if (verif.CredentialsVerification(args) == 0)
				return;

			String value = args[1];
			this.username = value.replaceAll("-u", "");

			System.out.print("{ 'status' : 'ok', 'message' : '[");
			this.SearchForQuiz(this.username);
			System.out.print("]'}");

		}
	}

	public static class GetQuizDetail {

		String username, id;

		public void WriteQuestionInfo(String questionId) {
			questionId = questionId.replace("'", "");
			questionId = questionId.replace(" ", "");

			try (BufferedReader br = new BufferedReader(new FileReader(("questions.csv")))) {
				String line;

				while ((line = br.readLine()) != null) {
					String[] values = line.split(",", 20);

					if (values[0].contains(questionId)) {

						int contor = 1; // for the placement of the coma

						System.out.print("{\"question-name\":\""+ values[3].substring(2, values[3].length() -1) +
								"\", \"question_index\":\""+questionId+"\", \"question_type\":\""+ values[4].substring(2, values[4].length() - 1)+
								"\", \"answers\":\"[");

						// for each answer of the question we print the name and id
						for (int j = 5; j < values.length - 1; j++) {

							// in the file the id, text and correct value are separated by "@"
							String[] name = values[j].split("@", 3);

							if(contor == 1) {
								System.out.print("{");
								System.out.print("\"answer_name\":\""+ name[0].substring(2, name[0].length() -1) +"\", \"answer_id\":\""+name[2]+"\"");
								System.out.print("}");
							} else {
								System.out.print(", {");
								System.out.print("\"answer_name\":\""+ name[0].substring(2, name[0].length() -1) +"\", \"answer_id\":\""+name[2]+"\"");
								System.out.print("}");
							}
							contor++;
						}
					}
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}

		public void GetQuiz (String id) {

			try (BufferedReader br = new BufferedReader(new FileReader(("quiz.csv")))) {
				String line;

				while ((line = br.readLine()) != null) {

					// get rid of the '
					String[] values = line.split(",", 20);
					id = id.replace(" '", "");
					id = id.replaceAll("'", "");

					int contorQuest = 1;
					if (id.contains(values[5])) {

						System.out.print("{'status':'ok','message':'[");

						// for each question of the quiz we call the function that prints its info
						for (int i = 3; i < values.length - 2; i++) {
							WriteQuestionInfo(values[i]);
							System.out.print("]\"}");

							if (contorQuest == 1) {
								System.out.print(", ");
							}
							contorQuest++;
						}
					}
					System.out.print("]'}");
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}

		public void VerifyAllDetail(final String args[]) {

			Tema1 verif = new Tema1();
			if (verif.CredentialsVerification(args) == 0)
				return;

			String value = args[1];
			this.username = value.replaceAll("-u", "");

			value = args[3];
			this.id = value.replaceAll("-id", "");

			// calling the method that iterates through the questions
			this.GetQuiz(id);

		}
	}

	public static class SubmitQuiz {

		String id;

		// method that returns the line with the quiz information
		public String ReturnQuizLine(String id) {

			try (BufferedReader br = new BufferedReader(new FileReader("quiz.csv"))) {
				String line;

				while ((line = br.readLine()) != null) {
					String[] values = line.split(",", 20);

					if (values[5].contains(id)) {
						return line;
					}
				}

			} catch (IOException e) {
				e.printStackTrace();
			}
			return "-1";
		}

		// method that returns the line of the question with the specified id
		public String ReturnQuestionLine(String id) {

			try (BufferedReader br = new BufferedReader(new FileReader("questions.csv"))) {
				String line;

				while ((line = br.readLine()) != null) {
					String[] values = line.split(",", 20);

					if (values[0].contains(id.substring(2, id.length() - 1))) {
						return line;
					}
				}

			} catch (IOException e) {
				e.printStackTrace();
			}
			return "-1";
		}

		// method that calculates if an answer is the correct one for the provided question
		public int Raspuns(String Question, String answerId) {

			String line = ReturnQuestionLine(Question);
			String[] question = line.split(",", 20);
			answerId = answerId.substring(2, answerId.length() - 1);

			for (int i = 5; i < question.length -1 ; i++ ) {
				String[] answer = question[i].split("@", 3);

				if (answer[1].contains("1") && answer[2].contains(answerId)) {
					return 1;
				}
			}
			return 0;
		}
		public int QuizCompleting(final String args[]) {

			String value = args[3];
			this.id = value.replaceAll("-quiz-id '", "");
			this.id = this.id.replace("'", "");

			String quiz = ReturnQuizLine(this.id);
			String[] quizLine = quiz.split(",", 20);

			int indexAnswer = 4;
			int aux = indexAnswer - 3;
			int is_correct = 0;

			// for each answer of the questions, the function return 1 (the answer is correct) or 0 otherwise
			for (int i = 3; i < quizLine.length - 2; i++) {

				is_correct += Raspuns(quizLine[i], args[indexAnswer].replaceAll("-answer-id-" + Integer.toString(aux) + " ", " "));
				indexAnswer++;
				aux++;
			}

			int nrOfQuestions = quizLine.length - 5;
			int pondere = 100 / nrOfQuestions;
			return pondere * is_correct;
		}

		public void VerifySubmit(final String args[]) {

			Tema1 verif = new Tema1();
			if (verif.CredentialsVerification(args) == 0)
				return;

			if (args.length < 4) {
				System.out.println("{ 'status' : 'error', 'message' : 'No quizz identifier was provided'}");
				return;
			}

			this.id = args[3].replace("-quiz-id ", "");
			id = id.substring(1, id.length() - 1);

			// if the id is bigger than the biggest id_quiz used at the moment
			if (Integer.parseInt(id) >= id_quiz) {
				System.out.print("{ 'status' : 'error', 'message' : 'No quiz was found'}");
				return;
			}

			// calling the method that calculates the score
			int score = QuizCompleting(args);
			System.out.print("{ 'status' : 'ok', 'message' : '" + score +" points'}");
		}
	}

	public static class DeleteQuiz {

		String username, password, id;

		// function for inserting one string in the file
		public void Insert(String info, String file) {
			try (FileWriter fw = new FileWriter(file, true);
				 BufferedWriter bw = new BufferedWriter(fw);
				 PrintWriter out = new PrintWriter(bw)) {

				out.print(info + ",");

			} catch (IOException e) {
				e.printStackTrace();
			}

		}

		// for each line that does not match the id it is inserted in the auxiliar file
		public void Delete(String id) {

			try (BufferedReader br = new BufferedReader(new FileReader("quiz.csv"))) {
				String line;
				while ((line = br.readLine()) != null) {
					String[] values = line.split(",", 20);


					if (!values[5].contains(id)) {
						for (int i = 0 ; i < values.length; i++) {
							Insert(values[i], "auxiliar.csv");
						}
						Insert("\n", "auxiliar.csv");
					}
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}

		// we delete the former file and insert the info from the aux (it does not have the specified line)
		public void RestoreFile() {

			File file = new File("quiz.csv");
			if (file.delete()) {
				System.out.print("{ 'status' : 'ok', 'message' : 'Cleanup finished successfully'}");
			}

			try (BufferedReader br = new BufferedReader(new FileReader("auxiliar.csv"))) {
				String line;
				while ((line = br.readLine()) != null) {
					String[] values = line.split(",", 20);


					if (!values[5].contains(id)) {
						for (int i = 0 ; i < values.length; i++) {
							Insert(values[i], "quiz.csv");
						}
						Insert("\n", "quiz.csv");
					}
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		public void VerifyDeleteQuiz(final String args[]) {

			Tema1 verif = new Tema1();
			if (verif.CredentialsVerification(args) == 0)
				return;

			if(args.length < 4) {
				System.out.print("{ 'status' : 'error', 'message' : 'No quizz identifier was provided'}");
			}

			if (args.length < 4) {
				return;
			}

			String value = args[3];
			this.id = value.replaceAll("-id '", "");
			this.id = this.id.replace("'", "");

			// if the id is bigger than the last id used at the moment
			if (Integer.parseInt(id) >= id_quiz) {
				System.out.print("{ 'status' : 'error', 'message' : 'No quiz was found'}");
				return;
			}

			Delete(this.id);
			System.out.print("{ 'status' : 'ok', 'message' : 'Quizz deleted successfully'}");
		}
	}

	public static class GetSolutions {
		public void VerifySolutions(final String args[]) {

			Tema1 verif = new Tema1();
			verif.CredentialsVerification(args);

		}
	}

	public static void main(final String[] args) {
		if (args == null) {

			System.out.print("Hello world!");
			return;
		}
		if (args[0].contains("-create-user")) {

			UserCommands commUser = new UserCommands();
			commUser.VerifyUsername(args);

		} else if (args[0].contains("-create-question")) {

			Questions commUser = new Questions();
			commUser.VerifyUsernameQuestion(args);

		} else if (args[0].contains("-cleanup-all")) {

			Delete del = new Delete();
			del.DeletingFiles();

			// reset the ids after deleting all of the files
			id_questions = 1;
			id_quiz = 1;
			id_answers = 1;

		} else if (args[0].contains("-get-question-id-by-text")) {

			GetQuestion getQ = new GetQuestion();
			getQ.Verify(args);

		} else if (args[0].contains("-get-all-questions")) {

			GetAllQuestions get = new GetAllQuestions();
			get.VerifyAll(args);

		} else if (args[0].contains("-create-quizz")) {

			CreateQuiz quiz = new CreateQuiz();
			quiz.Verify(args);

		} else if (args[0].contains("-get-quizz-by-name")) {

			GetQuiz quiz = new GetQuiz();
			quiz.Verify(args);

		} else if (args[0].contains("-get-all-quizzes")) {

			GetAllQuiz quiz = new GetAllQuiz();
			quiz.VerifyAll(args);

		} else if (args[0].contains("-get-quizz-details-by-id")) {

			GetQuizDetail quiz = new GetQuizDetail();
			quiz.VerifyAllDetail(args);

		} else if (args[0].contains("-submit-quizz")) {

			SubmitQuiz quiz = new SubmitQuiz();
			quiz.VerifySubmit(args);

		} else if (args[0].contains("-delete-quizz-by-id")) {

			DeleteQuiz quiz = new DeleteQuiz();
			quiz.VerifyDeleteQuiz(args);

		} else if (args[0].contains("-get-my-solutions")) {

			GetSolutions sol = new GetSolutions();
			sol.VerifySolutions(args);

		}
	}
}