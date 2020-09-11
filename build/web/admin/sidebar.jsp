<aside class="main-sidebar"> 
    <section class="sidebar"> 
        <div class="user-panel">
            <div class="pull-left image">
                <img src="assets/img/user.jpg" class="img-circle" alt="User Image">
            </div>
            <div class="pull-left info">
                <%
                    String firstName = (String) session.getAttribute("fName");
                    String lName = (String) session.getAttribute("lName");
                %>
                <p><%=firstName + " " + lName%></p>
                <a href="#"><i class="fa fa-circle text-success"></i> Online</a>
            </div>
        </div> 

        <ul class="sidebar-menu">
            <li class="header">MAIN NAVIGATION</li>
            <li class="active treeview">
                <a href="home.jsp"><i class="fa fa-dashboard"></i> <span>Dashboard</span></a>
                <a href="SetOldFpApiIp.jsp"><i class="fa fa-dashboard"></i> <span>Old Finger Matching API Info</span></a>
                <a href="dbInfo.jsp"><i class="fa fa-dashboard"></i> <span>Database Information</span></a>
                <a href="newAdmin.jsp"><i class="fa fa-dashboard"></i> <span>New Admin</span></a>
                <a href="matchingScore.jsp"><i class="fa fa-dashboard"></i> <span>Matching Score</span></a>
            </li> 
        </ul>
    </section> 
</aside>