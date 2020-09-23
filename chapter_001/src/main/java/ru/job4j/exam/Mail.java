package ru.job4j.exam;

import java.util.*;
import java.util.stream.Collectors;

public class Mail {
    private Map<User, Set<Email>> mapUser = new HashMap<>();

     public Map<User, Set<Email>> unitMail(Map<User, Set<Email>> accounts) {
        Set<Email> setEmails = new HashSet<>();
         for (User user : accounts.keySet()) {
             Set<Email> emails = accounts.get(user);
             for (Email tempEmail : emails) {
                 if (this.findUserByEmail(tempEmail) != null) {
                     User tempUser = this.findUserByEmail(tempEmail);
                     setEmails = this.findSetEmails(tempUser);
                   //  setEmails.add(tempEmail);
//                     emails = setEmails.stream().
//                             filter(e -> e.equals(tempEmail)).
//                             collect(Collectors.toSet());
                     mapUser.put(user, setEmails.stream().
                             filter(e -> !e.equals(tempEmail)).
                             collect(Collectors.toSet()));
                     mapUser.remove(tempUser, setEmails);

                 } else if (this.findSetEmails(user) == null) {
                     Set<Email> tempSet = new HashSet<>();
                     tempSet.add(tempEmail);
                     mapUser.put(user, tempSet);
                 } else {
                     setEmails = this.findSetEmails(user);
                     setEmails.add(tempEmail);
                 }
             }
         }
         return this.mapUser;
     }

     private Set<Email> findSetEmails(User user) {
         return this.mapUser.get(user);
     }

    private User findUserByEmail(Email email) {
    Iterator<User> it = this.mapUser.keySet().iterator();
    User user = null;
    Set<Email> setEmail;
    while (it.hasNext()) {
        User tempUser = it.next();
        setEmail = this.findSetEmails(tempUser);
        for (Email tempEmail : setEmail) {
            if (tempEmail.equals(email)) {
                user = tempUser;
                break;
            }
        }
    }
    return user;
     }


    public static class User {
         private String name;

         public User(String name) {
             this.name = name;
         }


        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }
    }

    public static class Email {
         private String email;

         public Email(String email) {
             this.email = email;
         }

        public String getEmail() {
            return email;
        }

        public void setEmail(String email) {
            this.email = email;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            Email email1 = (Email) o;
            return Objects.equals(email, email1.email);
        }

        @Override
        public int hashCode() {
            return Objects.hash(email);
        }
    }
}
