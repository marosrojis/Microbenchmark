export class ProcessInfo {
  public time: string;
  public operation: string;
  public number: number;
  public note: string;

  constructor(time: string, operation: string, number: number, note: string) {
    this.time = time;
    this.operation = operation;
    this.number = number;
    this.note = note;
  }
}
