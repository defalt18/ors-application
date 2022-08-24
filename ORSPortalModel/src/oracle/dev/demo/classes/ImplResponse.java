package oracle.dev.demo.classes;

public class ImplResponse {
    Exception error = null;
    Object response = null;
    String redirect = null;

    public void setRedirect(String redirect) {
        this.redirect = redirect;
    }

    public String getRedirect() {
        return redirect;
    }
    
    public void setError(Exception error) {
        this.error = error;
    }

    public Exception getError() {
        return error;
    }

    public void setResponse(Object response) {
        this.response = response;
    }

    public Object getResponse() {
        return response;
    }
}

