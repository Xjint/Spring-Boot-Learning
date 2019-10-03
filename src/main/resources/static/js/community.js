function post() {
    let questionId = $("#question_id").val();
    let content = $("#comment_content").val();
    if (!content) {
        alert("不能回复空内容！")
        return;
    }
    $.ajax({
        type: "POST",
        url: "/comment",
        contentType: "application/json",
        data: JSON.stringify({
            "parentId": questionId,
            "content": content,
            "type": 1
        }),
        success: function (response) {
            if (response.code === 200) {
                //提交回复成功后，直接刷新页面，做到实时显示回复的功能
                window.location.reload();
            } else {
                if (response.code === 2003) {
                    //登陆异常
                    let isAccepted = confirm(response.message);
                    if (isAccepted) {
                        window.open("https://github.com/login/oauth/authorize?client_id=3e9a0e7fcb670458c29a&redirect_uri=http://localhost:8080/callback&scope=user&state=1");
                        window.localStorage.setItem("closable", true);
                    }
                } else {
                    alert(response.message);
                }
            }
        },
        dataType: "json"
    });
}