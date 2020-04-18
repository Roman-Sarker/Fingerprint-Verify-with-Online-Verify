<!DOCTYPE html>
<html>
    <head>
        <meta charset="utf-8">
        <meta http-equiv="X-UA-Compatible" content="IE=edge">
        <title>Finger</title>
        <meta content="width=device-width, initial-scale=1, maximum-scale=1, user-scalable=no" name="viewport">
        <link rel="stylesheet" href="assets/css/bootstrap.min.css">
        <link rel="stylesheet" href="assets/css/AdminLTE.min.css">  <!-- Theme style -->
        <link rel="stylesheet" href="assets/css/_all-skins.min.css">
        <link rel="stylesheet" href="assets/css/blue.css">   <!-- iCheck -->
        <link rel="stylesheet" href="plugins/morris/morris.css">  <!-- Morris chart -->
        <link rel="stylesheet" href="plugins/jvectormap/jquery-jvectormap-1.2.2.css"> <!-- jvectormap -->
        <link rel="stylesheet" href="plugins/datepicker/datepicker3.css"> <!-- Date Picker -->
        <link rel="stylesheet" href="plugins/daterangepicker/daterangepicker-bs3.css">  <!-- Daterange picker -->
        <link rel="stylesheet" href="plugins/bootstrap-wysihtml5/bootstrap3-wysihtml5.min.css"> <!-- bootstrap wysihtml5 - text editor -->
        <script src="assets/js/jquery-3.2.1.min.js"></script>
        <script src="assets/bootstrap/js/bootstrap.min.js"></script>
        <script src="assets/js/app.min.js"></script>
        <script src="assets/js/demo.js"></script>
        <link rel="shortcut icon" href="assets/ico/favicon.png">
    </head>

    <body class="hold-transition skin-blue sidebar-mini">
        <%
            if (session.getAttribute("LOGIN") == null) {
                String redirectURL = "index.jsp";
                response.sendRedirect(redirectURL);
            }
            String contextPath = request.getContextPath();
        %>
        <div class="wrapper">
            <%
                String loginFlag = (String) session.getAttribute("LOGIN");
                if (loginFlag == null) {
                    String redirectURL = "index.jsp";
                    response.sendRedirect(redirectURL);
                }
            %>
            <jsp:include page="topbar.jsp" />
            <jsp:include page="sidebar.jsp" />

            <div class="content-wrapper"> 
                <section class="content"> 
                    <div class="row">
                        <div class="col-sm-6 col-sm-offset-3 l-form-9-left section-description wow fadeIn">
                            <%                                String error_msg = (String) request.getParameter("eMessage");
                                String sucess_msg = (String) request.getParameter("sMessage");

                                if (error_msg != null) {%>
                            <div class="alert alert-danger">
                                <%=error_msg%> 
                            </div>
                            <%
                            } else if (sucess_msg != null) {%>
                            <div class="alert alert-success">
                                <%=sucess_msg%> 
                            </div>
                            <% }
                            %>

                        </div> 
                    </div>
                    <div class="box">
                        <div id="signupbox" style="margin-top:50px" class="mainbox col-md-6 col-md-offset-3 col-sm-8 col-sm-offset-2">
                            <div class="panel panel-info">
                                <div class="panel-heading">
                                    <div class="panel-title">Fixed Match Score.</div>
                                </div>
                                <div class="panel-body" >
                                    <form action="<%=contextPath%>/AdminMatchScore" role="form" method="post">
                                        <div id="signupalert" style="display:none" class="alert alert-danger">
                                            <p>Error:</p>
                                            <span></span>
                                        </div>
                                        

                                        <div class="form-group">
                                            <label for="userNameLabel" class="col-md-3 control-label">Match Score </label>
                                            <div class="col-md-9">
                                                <select id="matchValue" name="matchValue"  style="color: white; background-color: #5dade2
                                                        ; width:300px; height: 40px; font-weight: bold;font-size: 15px;text-align: center;">
                                                    <option value="37">Low (37 - 1%)</option>
                                                    <option value="65">Low Medium (65 - 0.1%)</option>
                                                    <option value="93">Medium (93 - 0.01%)</option>
                                                    <option value="121">High Medium (121 - 0.001%)</option>
                                                    <option value="146">High (146 - 0.0001%)</option>
                                                    <option value="189">Very High (189 - 0%)</option>
                                                    <option value="0" selected>-- Select Matching Score--</option>
                                                </select>
                                            </div>
                                        </div>
                                        





                                        <div class="form-group">
                                            <!-- Button -->
                                            <div class="col-md-offset-6 col-md-6">
                                                <button id="btn-signup" type="submit" class="btn btn-info" style="float: right;"> &nbsp &nbsp Save &nbsp &nbsp</button>
                                            </div>
                                        </div>
                                    </form>
                                </div>
                            </div>
                        </div>  
                    </div>
                </section> 
            </div>  
            <jsp:include page="footer.jsp" />
            <div class="control-sidebar-bg"></div>
        </div>
    </body>
</html>
