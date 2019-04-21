export class ProcessInfo {
  public time: string;
  public estimatedEndTime: string;
  public operation: string;
  public number: number;
  public note: string;

  constructor(time: string, estimatedEndTime: string, operation: string, number: number, note: string) {
    this.time = time;
    this.estimatedEndTime = estimatedEndTime;
    this.operation = operation;
    this.number = number;
    this.note = note;
  }
}
