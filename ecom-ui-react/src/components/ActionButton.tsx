import type { AnchorHTMLAttributes, ReactNode } from 'react'
import type { IconName } from '../assets/icons'
import Icon from './Icon'

type Variant = 'outline-light' | 'solid-brand' | 'solid-light'

const variantClasses: Record<Variant, string> = {
  'outline-light': 'border border-white text-white hover:bg-white/10',
  'solid-brand': 'bg-brand text-white hover:brightness-95',
  'solid-light': 'bg-white text-navy-deep hover:bg-white/90',
}

type ActionButtonProps = AnchorHTMLAttributes<HTMLAnchorElement> & {
  variant: Variant
  icon?: IconName
  children: ReactNode
}

/** 42px-tall rectangular call-to-action used in the header and sign-up banner. */
export default function ActionButton({
  variant,
  icon,
  children,
  className = 'w-[170px]',
  ...rest
}: ActionButtonProps) {
  return (
    <a
      {...rest}
      className={`flex h-[42px] shrink-0 items-center justify-center gap-[10px] rounded-[4px] px-[10px] py-[6px] text-[14px] font-medium whitespace-nowrap transition focus-visible:outline-2 focus-visible:outline-offset-2 focus-visible:outline-white ${variantClasses[variant]} ${className}`}
    >
      {icon && <Icon name={icon} />}
      <span>{children}</span>
    </a>
  )
}
