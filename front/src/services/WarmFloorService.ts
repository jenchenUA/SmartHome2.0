import { Client } from '@stomp/stompjs';
import EventBus from "./EventBus";
import WarmFloorData from "../components/warmfloorcard/WarmFloorData";
import WarmFloorConfiguration from "../components/warmfloorcreateform/WarmFloorConfiguration";

export default class WarmFloorService {

    private static instance: WarmFloorService;
    private client: Client | undefined;
    private host = `${window.location.protocol}//${window.location.host}/api`;

    constructor() {
        this.client = new Client({
            brokerURL: `ws://${window.location.host}/updates`,
            reconnectDelay: 5000,
            heartbeatIncoming: 4000,
            heartbeatOutgoing: 4000,
            onConnect: () => this.client?.subscribe( '/warm-floor', message => {
                const body : WarmFloorData = JSON.parse(message.body);
                EventBus.getInstance().dispatch(`warmfloor${body.id}`, body);
            }),
            onDisconnect: () => this.client?.unsubscribe("/warm-floor")
        });
    }


    public static getInstance() : WarmFloorService {
        if (!this.instance) {
            this.instance = new WarmFloorService();
        }
        return this.instance;
    }


    public async createWarmFloorConfig(config: WarmFloorConfiguration) {
        return await fetch(`${this.host}/warm-floor`, {method: "POST", body: JSON.stringify(config),
            headers: {"Content-Type": "application/json"}})
            .then(res => {
                if (!res.ok) {
                    return res.text().then(text => {throw new Error(text)})
                }
                return res.json();
            })
    }

    public async getAllWarmFloors() {
        return await fetch(`${this.host}/warm-floor`)
            .then(res => res.json())
    }

    public async toggle(id: number) {
        await fetch(`${this.host}/warm-floor/${id}/toggle`, {method: "PUT"})
    }

    public updateThreshold(id: number, newThreshold: number) {
        const thresholdData = {newThreshold: newThreshold};
        fetch(`${this.host}/warm-floor/${id}/threshold`,
            {method: "PUT", body: JSON.stringify(thresholdData), headers: {"Content-Type": "application/json"}})
    }

    public async removeWarmFloorConfig(id: number) {
        return await fetch(`${this.host}/warm-floor/${id}`, {method: "DELETE"})
    }

    public listenUpdates() {
        this.client?.activate();
    }

    public stopListeningUpdates() {
        this.client?.deactivate();
    }

    private onDisconnect(): void {
    }
}