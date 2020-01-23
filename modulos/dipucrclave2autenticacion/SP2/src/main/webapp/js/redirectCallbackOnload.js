// Automatic Redirect of the form
function submitRedirectFormAction() {
document.getElementsByName('redirectForm')[0].submit();
}
window.addEventListener('load', submitRedirectFormAction());
