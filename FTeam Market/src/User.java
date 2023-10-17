
public class User {
	  private String userid;
      private String username;
      private String password;
      private String email;
      private String phonenumber;
      private String age;
      private String gender;
      
      public User(String userid, String username, String password, String email, String phonenumber, String age, String gender) {
          this.userid = userid;
          this.username = username;
          this.password = password;
          this.email = email;
          this.phonenumber = phonenumber;
          this.age = age;
          this.gender = gender;
      }

      public String getUserid() {
          return userid;
      }

      public void setUserid(String userid) {
          this.userid = userid;
      }

      public String getUsername() {
          return username;
      }

      public void setUsername(String username) {
          this.username = username;
      }

      public String getPassword() {
          return password;
      }

      public void setPassword(String password) {
          this.password = password;
      }

      public String getEmail() {
          return email;
      }

      public void setEmail(String email) {
          this.email = email;
      }

      public String getPhonenumber() {
          return phonenumber;
      }

      public void setPhonenumber(String phonenumber) {
          this.phonenumber = phonenumber;
      }

      public String getAge() {
          return age;
      }

      public void setAge(String age) {
          this.age = age;
      }

      public String getGender() {
          return gender;
      }

      public void setGender(String gender) {
          this.gender = gender;
      }
}
