<g:if test="${flash.error}">
    <div class="row alert alert-danger">
        <div class="col-sm-12">
            <h4>
                ${flash.error}
            </h4>
        </div>
    </div>
</g:if>

<g:if test="${flash.info}">
    <div class="row alert alert-info">
        <div class="col-sm-12">
            <h4>
                ${flash.info}
            </h4>
        </div>
    </div>
</g:if>

<g:if test="${flash.success}">
    <div class="row alert alert-success">
        <div class="col-sm-12">
            <h4>
                ${flash.success}
            </h4>
        </div>
    </div>
</g:if>

<g:if test="${flash.warning}">
    <div class="row alert alert-warning">
        <div class="col-sm-12">
            <h4>
                ${flash.warning}
            </h4>
        </div>
    </div>
</g:if>