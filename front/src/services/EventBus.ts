export default class EventBus {

    private static instance: EventBus;

    public static getInstance() : EventBus {
        if (!this.instance) {
            this.instance = new EventBus();
        }
        return this.instance;
    }

    onEvent(event: string, listener: EventListener) {
        document.addEventListener(event, listener);
    }

    dispatch(event: string, payload: any) {
        document.dispatchEvent(new CustomEvent(event, {detail: payload}));
    }

    stopListening(event: string, listener: EventListener) {
        document.removeEventListener(event, listener);
    }
}
