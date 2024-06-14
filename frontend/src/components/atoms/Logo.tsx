import { FC } from "react"
import defaultLogo from "public/bill-one-logo.svg"
type LogoProps = {
    src?: string
}

export const Logo: FC<LogoProps> = ({ src }) => {
    return (
        <img style={{padding:"2% 1%"}} src={src ?? defaultLogo} alt="logo"/>
    )
}
