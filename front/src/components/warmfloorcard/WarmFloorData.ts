export default interface WarmFloorData {
    id: number,
    name: string
    currentTemperature: number,
    threshold: number,
    enabled: boolean,
    heatingEnabled: boolean,
}

export interface WarmFloorProperties {
    cardData: WarmFloorData,
    deleteCallback: Function,
}
