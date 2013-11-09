<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Insert title here</title>
</head>
<body>
<form action="GreetingServlet" method="POST">
        First Name: <input type="text" name="firstName" size="20"><br>
        Surname: <input type="text" name="surname" size="20">
        selections: <input type="text" name="selections" size="220">
        <br><br>
        <input type="submit" value="Submit">
    </form>
</body>
</html>