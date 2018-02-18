<!-- There is login form -->
<div class="container">
    <form class="form-signin" action="/login" method="post">
        <h2 class="form-signin-heading">Please sign in</h2>
        <label for="login" class="sr-only">Your login</label>
        <input id="login" name="login" class="form-control" placeholder="Login" required autofocus>
        <label for="pass" class="sr-only">Password</label>
        <input type="password" id="pass" name="password" class="form-control" placeholder="Password" required>
        <#if error??>
        <div class="alert alert-danger">
            ${error}
        </div>
        </#if>
        <button class="btn btn-lg btn-primary btn-block" type="submit">Sign in</button>
    </form>
</div>
