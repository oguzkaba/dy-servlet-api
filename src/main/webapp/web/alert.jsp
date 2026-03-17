<%@ page import="com.kodlabs.doktorumyanimda.utils.Functions" %>
<div id="messageModal" class="modal" tabindex="-1">
    <div class="modal-dialog">
        <div class="modal-content">
            <div class="modal-header">
                <h5 class="modal-title" ng-bind="modalTitle"></h5>
            </div>
            <div class="modal-body">
                <p  ng-bind="modalContent"></p>
            </div>
            <div class="modal-footer">
                <button type="button" class="btn btn-danger" data-bs-dismiss="modal">Kapat</button>
            </div>
        </div>
    </div>
</div>
<%
    Boolean isSuccess = (Boolean)request.getSession().getAttribute("isSuccess");
    String message = (String)request.getSession().getAttribute("message");
    if(isSuccess != null && message != null && !isSuccess){

%>
        <script type="text/javascript">
            const message = '<%=message%>';
            const error = new Modal(document.querySelector('#errorModal'));
            document.querySelector('#errorModalContent').innerHTML = message;
            error.toggle();
        </script>
<%
        request.getSession().setAttribute("isSuccess",null);
        request.getSession().setAttribute("message",null);
    }
%>