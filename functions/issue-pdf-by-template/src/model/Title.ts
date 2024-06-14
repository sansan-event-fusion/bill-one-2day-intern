export type TitleArgs = {
    title:string
}

export class Title {
    private title:string

    constructor(args:string){
        this.title = args
    }

    public getTitle():string{
        return this.title
    }
}