export class LibrariesToChoose {
  public projectId: string;
  public imports: Map<string, string[]>;

  constructor(projectId: string, imports: Map<string, string[]>) {
    this.projectId = projectId;
    this.imports = imports;
  }
}
