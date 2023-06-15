public class StatusResponse {
    private ScanStatus status;

    public StatusResponse(ScanStatus status) {
        this.status = status;
    }

    public ScanStatus getStatus() {
        return status;
    }

    public void setStatus(ScanStatus status) {
        this.status = status;
    }
}
