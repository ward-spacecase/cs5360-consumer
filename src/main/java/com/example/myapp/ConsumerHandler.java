package com.example.myapp;

public class ConsumerHandler {

    private final String bucket2 = "usu-cs5260-redwood-requests";
    private final String bucket3 = "usu-cs5260-redwood-web";
    private final int WAIT_TIME_MS = 100;
    private final Service _service = new Service();

    public void consumerLoop() {

        while(true) {
            WidgetRequest widgetRequest = this.getNextWidgetRequest();
            this.deleteWidgetRequest(widgetRequest.getKey());
            this.putWidget(widgetRequest);
            //pauseRequests();
        }
    }

    /*
     * 
     */
    private WidgetRequest getNextWidgetRequest() {
       return _service.getWidgetRequest(bucket2);
    }

    /**
     * 
     * @param widgetRequestId
     */
    private void deleteWidgetRequest(String widgetRequestId) {
        _service.deleteWidgetRequest(bucket2, widgetRequestId);
    }

    /*
     * 
     */
    private void pauseRequests() {
        try {
            Thread.sleep(WAIT_TIME_MS);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    private void putWidget(WidgetRequest widgetRequest) {
        _service.putWidget(bucket3, widgetRequest);
    }
}
