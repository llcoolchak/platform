$(document).ready(function() {
    $('#saveDoc').click(function() {
        $("#addNewDoc").validate();
        if ($("#addNewDoc").valid()) {
            var api=$("#item-info h2")[0].innerHTML;
            var apiName = $.trim(api.split("-")[0]);
            var version = $.trim(api.split("-")[1]);
            var provider = $("#spanProvider").text();
            var docName = $("#docName").val();
            var summary = $("#summary").val();
            var docType = getRadioValue($('input[name=optionsRadios]:radio:checked'));
            var sourceType = getRadioValue($('input[name=optionsRadios1]:radio:checked'));
            var docUrl = $("#docUrl").val();
            if (docUrl.indexOf("http") == -1) {
                docUrl = "http://" + docUrl;
            }
            jagg.post("/site/blocks/documentation/ajax/docs.jag", { action:"addDocumentation",
                provider:provider,apiName:apiName, version:version,docName:docName,docType:docType,summary:summary,sourceType:sourceType,
                docUrl:docUrl},
                      function (result) {
                          if (!result.error) {
                              clearDocs();
                              $.cookie("tab", "docsLink");
                              window.location.reload();
                          } else {
                              jagg.message({content:result.message,type:"error"});
                          }
                      }, "json");
        }
    });
});


var removeDocumentation = function (provider, apiName, version, docName, docType) {
    $('#messageModal').html($('#confirmation-data').html());
    $('#messageModal h3.modal-title').html('Confirm Delete');
    $('#messageModal div.modal-body').html('\n\nAre you sure you want to delete the file <b>"' + docName + '</b>"?');
    $('#messageModal a.btn-primary').html('Yes');
    $('#messageModal a.btn-other').html('No');
    $('#messageModal a.btn-primary').click(function() {
        jagg.post("/site/blocks/documentation/ajax/docs.jag", { action:"removeDocumentation",provider:provider,
            apiName:apiName, version:version,docName:docName,docType:docType},
                  function (result) {
                      if (!result.error) {
                          $('#messageModal').modal('hide');
                          $('#' + apiName + '-' + docName).remove();
                          if ($('#docTable tr').length == 1) {
                              $('#docTable').append($('<tr><td colspan="6">No documentation associated with the API</td></tr>'));
                          }
                      } else {
                          jagg.message({content:result.message,type:"error"});
                      }
                  }, "json");
    });
    $('#messageModal a.btn-other').click(function() {
        return;
    });
    $('#messageModal').modal();
};

var updateDocumentation = function (docName, docType, summary, docUrl) {
    $('#newDoc .btn-primary').text('Update');
    $('#newDoc').show('slow');
    $('#newDoc #docName').val(docName);
    if (summary != "{}") {
        $('#newDoc #summary').val(summary);
    }
    if (docUrl != "{}") {
        $('#newDoc #docUrl').val(docUrl);
        $('#optionsRadios8').attr('checked', true);
        $('#newDoc #docUrl').show();
    }

    for (var i = 1; i <= 6; i++) {
        if ($('#optionsRadios' + i).val().toUpperCase().indexOf(docType.toUpperCase()) >= 0) {
            $('#optionsRadios' + i).attr('checked', true);
        }
    }
};

var editInlineContent = function (provider, apiName, version, docName, mode) {
    var current = window.location.pathname;
    if (current.indexOf("item-info.jag") >= 0) {
        window.open("inline-editor.jag?docName=" + docName + "&apiName=" + apiName + "&version=" + version + "&provider=" + provider + "&mode=" + mode);
    } else {
        window.open("site/pages/inline-editor.jag?docName=" + docName + "&apiName=" + apiName + "&version=" + version + "&provider=" + provider + "&mode=" + mode);
    }

};

var clearDocs = function () {
    var doc = document;
    doc.getElementById('docName').value = '';
    doc.getElementById('summary').value = '';
    doc.getElementById('docUrl').value = '';
    $('#newDoc').hide('slow');

};

var getRadioValue = function (radioButton) {
    if (radioButton.length > 0) {
        return radioButton.val();
    }
    else {
        return 0;
    }
};




