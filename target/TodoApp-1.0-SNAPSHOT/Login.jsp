<%--
  Created by IntelliJ IDEA.
  User: marc_
  Date: 04.12.2018
  Time: 20:02
  To change this template use File | Settings | File Templates.
--%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE html>

<html>
<head>
    <meta charset = "UTF-8">
    <title>The Todo Application</title>

    <style>
        html, body {font-family: Helvetica;margin: 0; padding: 0; height: 100%;}
        #container {background: white; position: relative; min-height: 100%;}
        #header {background: orange; position: fixed; top: 0; width: 100%; height: 50px; overflow: hidden;}
        #body {background: white; padding-top: 50px;}
        #footer {background: orange; position: absolute; bottom: 0; width: 100%;}

        .inhalt {width: 90%; margin-top:0; margin-left:5%;}
        .inhalt_header {vertical-align: middle; line-height: 50px;}
        .inhalt_header_font {margin: 0; padding: 0;}
        .inhalt_footer {text-align: center; vertical-align: middle;}
        .inhalt_footer_font {margin: 0; padding: 0; }

        @media only screen and (min-width: 751px) {
            #body {padding-bottom: 30px;}
            #footer {height: 30px;}
            .inhalt_footer {height:20px; margin-top:5px;}
        }

        @media only screen and (min-width: 401px) and (max-width: 750px) {
            #body {padding-bottom: 50px;}
            #footer {height: 50px;}
            .inhalt_footer {height:40px; margin-top:5px;}
        }

        @media only screen and (max-width: 400px) {
            #body {padding-bottom: 70px;}
            #footer {height: 70px;}
            .inhalt_footer {height:60px; margin-top:5px;}
        }

        .login_registration {background: white; margin: auto; text-align: center;}

        .error {color: red}

        @media only screen and (min-width: 700px) {
            .login_registration {width: 700px;}
        }

        @media only screen and (max-width: 699px) {
            .login_registration {width: 100%;}
        }

        ul li:before { content:"\2714\0020"; }
        ul { list-style-type: none; }

        .login_registration_label {background: orange; border-radius: 10px;}
        .login_registration_form {border-style: solid; border-width: 3px; border-color: orange; padding: 10px; border-radius: 10px;}
    </style>



    <script type="text/javascript">
        function myFunction(input) {
            var x = document.getElementById("login_form");
            var y = document.getElementById("registration_form");

            if (input == 1 && x.style.display === "none") {
                x.style.display = "block";
                y.style.display = "none";
            }

            if (input == 2 && y.style.display === "none") {
                x.style.display = "none";
                y.style.display = "block";
            }
        }
    </script>
</head>


<body>

<div id="container">
    <div id="header">
        <div class = "inhalt inhalt_header">
            <h1 class = "inhalt_header_font">Welcome to the Todo Application</h1>
        </div>
        <br>
    </div>
    <div id="body">

        <div class = "inhalt">
            <section>
                <h1>What you can do</h1>
                <h1 style="font-size: 120%;"><ul>
                    <li>Create  Todos</li>
                    <li>Manage and filter Todos</li>
                    <li>Stay well rested with a built in REST-API</li>
                </ul></h1>
                <p></p>
            </section>

            <section>
                <h1>Login / Register</h1>

                <div class = "login_registration">
                    <div class = "login_registration_label" onclick="myFunction(1)" style="cursor:pointer;">
                        <h1>Login</h1>
                    </div>
                    <div class = "login_registration_form" id = "login_form" style="display: block;">
                        <form action="LoginAndRegister.do" method="post">
                            <label value = "name_login">Login-Name</label><br>
                            <input name = "name_login" style = "width: 90%;" required><br>
                            <label value = "passwd_login">Password</label><br>
                            <input type = "password" name = "passwd_login" style = "width: 90%;" maxlength = "40" required><br><br>
                            <input type="submit" name="login" value="Login"/>
                        </form>
                    </div>
                    <c:if test="${not empty errorMessage}">
                        <div class="error">
                            <c:out value="${errorMessage}"/>
                        </div>
                    </c:if>
                    <div class = "login_registration_label" onclick="myFunction(2)" style="cursor:pointer;">
                        <h1>Register</h1>
                    </div>

                    <div class = "login_registration_form" id = "registration_form" style="display: none;">
                        <form action="LoginAndRegister.do" method="post">
                            <label value = "name_registration">Your name</label><br>
                            <input name = "name_registration" style = "width: 90%;" maxlength = "50" required><br>
                            <label value = "passwd_registration">Password</label><br>
                            <input type= "password" name = "passwd_registration" style = "width: 90%;" maxlength = "40" required><br><br>
                            <input type="submit" name="login" value="Register"/>
                        </form>
                    </div>




                </div>
            </section>

        </div>
    </div>
    <div id="footer">
        <div class = "inhalt inhalt_footer">
            <p class = "inhalt_footer_font">&copy Copyright 2018. All rights reserved. Powered by the BFH - CAS Software Development.</p>
        </div>
    </div>
</div>


</body>
</html>