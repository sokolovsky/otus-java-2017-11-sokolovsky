<div class="container">
    <nav class="navbar navbar-inverse" role="navigation">
        <div class="container-fluid">
            <!-- Collect the nav links, forms, and other content for toggling -->
            <div class="collapse navbar-collapse" id="bs-example-navbar-collapse-9">
                <ul class="nav navbar-nav">
                    <li><a href="/">Users</a></li>
                    <li><a href="/chat">Chat room</a></li>
                </ul>
            </div><!-- /.navbar-collapse -->
        </div><!-- /.container-fluid -->
    </nav>
    <h1>${title}</h1>
    <table class="table">
        <thead>
            <tr>
                <th>Key</th>
                <th>Value</th>
            </tr>
        </thead>
        <tbody>
            <#list list as k, v>
                <tr>
                    <td>${k}</td>
                    <td>${v}</td>
                </tr>
            </#list>
        </tbody>
    </table>
</div>
