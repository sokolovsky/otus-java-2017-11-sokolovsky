<div class="container">

    <h1>Cache view</h1>

    <div class="row">
        <div class="col-2">Key</div>
        <div class="col-10">Value</div>
    </div>
    <#list list as k, v>
        <div class="row">
            <div class="col-2">${k}</div>
            <div class="col-10">${v}</div>
        </div>
    </#list>
</div>
<script type="text/javascript" src="/assets/js/refresh.js"></script>
